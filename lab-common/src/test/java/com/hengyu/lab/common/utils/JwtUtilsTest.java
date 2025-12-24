package com.hengyu.lab.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtUtilsTest {

  private JwtUtils jwtUtils;

  // 必须足够长，否则 JJWT 0.11+ 会报错 (HS256 需要至少 32 bytes)
  private static final String TEST_SECRET = "TestSecretKeyMustBeVeryLongToPassTheSecurityCheck123456";
  private static final long TEST_EXPIRATION = 3600000L; // 1小时
  private static final String TEST_PREFIX = "Bearer ";

  @BeforeEach
  void setUp() {
    // 手动实例化，模拟 Spring 的属性注入
    jwtUtils = new JwtUtils();
    jwtUtils.setSecret(TEST_SECRET);
    jwtUtils.setExpiration(TEST_EXPIRATION);
    jwtUtils.setTokenPrefix(TEST_PREFIX);
  }

  @Test
  @DisplayName("测试：正常生成并解析 Token")
  void shouldCreateAndParseTokenSuccessfully() {
    // Given
    String username = "hengyu";
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", 10086L);
    claims.put("role", "ADMIN");

    // When: 生成 Token
    String token = jwtUtils.createToken(username, claims);

    // Then: 验证 Token 生成了且不为空
    Assertions.assertNotNull(token);
    Assertions.assertFalse(token.isEmpty());

    // When: 解析 Token (模拟前端传来的带 Bearer 前缀的字符串)
    String tokenWithPrefix = TEST_PREFIX + token;
    Claims parsedClaims = jwtUtils.parseToken(tokenWithPrefix);

    // Then: 验证解析出的数据完全一致
    Assertions.assertEquals(username, parsedClaims.getSubject());
    // 注意类型转换：JSON 解析数字默认可能是 Integer，这里断言值相等即可
    Assertions.assertEquals(10086, parsedClaims.get("userId", Number.class).intValue());
    Assertions.assertEquals("ADMIN", parsedClaims.get("role"));
  }

  @Test
  @DisplayName("测试：Token 过期应该抛出异常")
  void shouldThrowExceptionWhenTokenExpired() {
    // Given: 设置一个极短的过期时间 (比如 1毫秒)
    jwtUtils.setExpiration(1L);

    String token = jwtUtils.createToken("hengyu", new HashMap<>());


    // When & Then: 解析应该报错 ExpiredJwtException
    Assertions.assertThrows(ExpiredJwtException.class, () -> {
      jwtUtils.parseToken(token);
    });
  }

  @Test
  @DisplayName("测试：Token 被篡改应该抛出异常")
  void shouldThrowExceptionWhenTokenIsTampered() {
    // Given
    String token = jwtUtils.createToken("hengyu", new HashMap<>());

    // 恶意篡改：删掉最后一个字符，或者随便改一个字母
    String tamperedToken = token.substring(0, token.length() - 1) + "x";

    // When & Then: 签名不匹配，应该报错 SignatureException (或 MalformedJwtException)
    // 注意：JJWT 0.11+ 可能会抛出不同的解码异常，这里捕获 RuntimeException 也可以，但最好精确
    Assertions.assertThrows(Exception.class, () -> {
      jwtUtils.parseToken(tamperedToken);
    });
  }

  @Test
  @DisplayName("测试：解析不带 Bearer 前缀的 Token 也能成功")
  void shouldParseTokenWithoutPrefix() {
    // Given
    String token = jwtUtils.createToken("hengyu", new HashMap<>());

    // When: 直接传入纯 Token (不加 Bearer)
    Claims claims = jwtUtils.parseToken(token);

    // Then
    Assertions.assertEquals("hengyu", claims.getSubject());
  }
}