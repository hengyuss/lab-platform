package com.hengyu.lab.framework.security;

import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.framework.redis.RedisCache;
import com.hengyu.lab.framework.utils.JwtUtils;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

  @MockBean
  private RedisTemplate redisTemplate;

  @Mock
  private RedisCache redisCache;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;


  @InjectMocks
  @Spy
  private TokenService tokenService;

  @Captor
  private ArgumentCaptor<Map<String, Object>> captor;

  // 模拟配置文件里的过期时间
  private final int MOCK_EXPIRE_TIME = 30;

  @BeforeEach
  void setUp() {
    // ✨ 关键点：使用反射工具给 private 字段赋值
    // 因为这是单元测试，@Value 不会生效，必须手动塞值
    ReflectionTestUtils.setField(tokenService, "expireTime", MOCK_EXPIRE_TIME);
  }


  @Test
  void test_createToken() {

    AuthUser authUser = AuthUser.builder()
        .username("testUsername")
        .id(100L)
        .identityType(1)
        .authorities(Collections.emptyList()).build();
    String expectedToken = "mock-jwt-token";
    when(jwtUtils.createToken(eq("testUsername"), Mockito.anyMap())).thenReturn(expectedToken);

    String actualToken = tokenService.createToken(authUser);

    Assertions.assertEquals(expectedToken, actualToken);

    verify(jwtUtils).createToken(eq("testUsername"), captor.capture());
    Map<String, Object> map = captor.getValue();

    Assertions.assertEquals(100L, map.get(AuthConstants.LOGIN_USER_ID));
    Assertions.assertEquals(1, map.get(AuthConstants.LOGIN_USER_ROLE));

  }

  @Test
  void test_refreshToken() {

    Long loginTime = System.currentTimeMillis();
    Long expireTime = System.currentTimeMillis() + MOCK_EXPIRE_TIME * 60 * 1000;

    AuthUser authUser = AuthUser.builder()
        .username("testUsername")
        .id(100L)
        .identityType(1)
        .authorities(Collections.emptyList())
        .loginTime(loginTime)
        .expireTime(expireTime)
        .uniqueKey("123")
        .build();

    String userKey = ReflectionTestUtils.invokeMethod(tokenService, "getTokenKey", authUser.getUniqueKey());

    tokenService.refreshToken(authUser);

    verify(redisCache).setCacheObject(userKey, authUser, MOCK_EXPIRE_TIME, TimeUnit.MINUTES);
    Assertions.assertTrue(Math.abs(System.currentTimeMillis() - loginTime) < 5000);
    Assertions.assertTrue(Math.abs(expireTime - authUser.getLoginTime()) > 29 * 60 * 1000);
  }


  @Test
  void test_get_user() {
    String token = "Bearer mock-jwt-token";
    when(request.getHeader("Authorization")).thenReturn(token);

    DefaultClaims claims = new DefaultClaims();
    claims.setSubject("testUsername");
    claims.put(AuthConstants.LOGIN_USER_KEY, "123");

    AuthUser authUser = AuthUser.builder()
        .username("testUsername")
        .id(100L)
        .authorities(Collections.emptyList())
        .uniqueKey("123")
        .build();
    when(redisCache.getCacheObject(AuthConstants.LOGIN_TOKEN_KEY + "123")).thenReturn(authUser);
    when(jwtUtils.parseToken("mock-jwt-token")).thenReturn(claims);

    AuthUser user = tokenService.getUser(request);

    verify(redisCache).getCacheObject(AuthConstants.LOGIN_TOKEN_KEY + authUser.getUniqueKey());

    Assertions.assertEquals(user.getUserId(), authUser.getUserId());
    Assertions.assertEquals(user.getUsername(), authUser.getUsername());

  }

  @Test
  @DisplayName("测试：解析Token抛出异常时，应该进入catch并返回null")
   void getUser_WhenException_ShouldReturnNull() {
    // 1. 准备 Request (必须有 Token，否则进不去 if 判断)
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer invalid-token-string");

    // 2. 🔥 关键步骤：强行让 jwtUtils 抛出异常
    // 告诉 Mockito：当调用 parseToken 时，不管参数是啥，直接给我抛个异常出来！
    when(jwtUtils.parseToken(anyString())).thenThrow(new RuntimeException("Token解析失败模拟"));


    // 3. 执行方法
    AuthUser result = tokenService.getUser(request);

    // 4. 断言
    // 因为你的 catch 块捕获了异常并打印了日志，代码会继续往下走，最终 return null
    assertNull(result, "当发生异常时，应该返回 null");
  }

  @Test
  void getUser_when_tokenIsNull_ShouldReturnNull() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    AuthUser result = tokenService.getUser(request);
    assertNull(result, "token为Null时，应该返回 null");
  }




  @Test
  void verify_token_refresh_token() {
    Long loginTime = System.currentTimeMillis() - 20 * 60 * 1000;
    Long expireTime = loginTime + MOCK_EXPIRE_TIME * 60 * 1000;
    Long expectExpireTime = System.currentTimeMillis() + MOCK_EXPIRE_TIME * 60 * 1000;
    AuthUser authUser = AuthUser.builder()
        .username("testUsername")
        .id(100L)
        .loginTime(loginTime)
        .expireTime(expireTime)
        .build();

    tokenService.verifyToken(authUser);

    Assertions.assertTrue(Math.abs(System.currentTimeMillis() - authUser.getLoginTime()) < 5000);
    Assertions.assertTrue(Math.abs(expectExpireTime - authUser.getExpireTime()) < 5000);
  }

  @Test
  void verify_token_not_refresh_token() {
    Long loginTime = System.currentTimeMillis();
    Long expireTime = loginTime + MOCK_EXPIRE_TIME * 60 * 1000;
    Long expectExpireTime = System.currentTimeMillis() + MOCK_EXPIRE_TIME * 60 * 1000;
    AuthUser authUser = AuthUser.builder()
        .username("testUsername")
        .id(100L)
        .loginTime(loginTime)
        .expireTime(expireTime)
        .build();

    tokenService.verifyToken(authUser);

    verify(tokenService, never()).refreshToken(authUser);

    Assertions.assertTrue(Math.abs(System.currentTimeMillis() - authUser.getLoginTime()) < 5000);
    Assertions.assertTrue(Math.abs(expectExpireTime - authUser.getExpireTime()) < 5000);
  }

  @Test
  void del_login_user_token_not_null() {
    String uuid = "123";

    tokenService.delLoginUser(uuid);

    verify(redisCache).deleteObject(anyString());

  }

  @Test
  void del_login_user_token_null() {
    String uuid = null;

    tokenService.delLoginUser(uuid);

    verify(redisCache, never()).deleteObject(anyString());
  }

  @ParameterizedTest(name = "输入Header: [{0}] => 期望结果: [{1}]")
  @CsvSource({
      // 场景1: 正常情况 (带前缀) -> 去掉前缀
      "Bearer my-token-123,   my-token-123",

      // 场景2: 没有前缀 -> 原样返回 (根据你的代码逻辑)
      "Basic my-token-123,    Basic my-token-123",

      // 场景3: 只有前缀 -> 返回空串
      "'Bearer ',               ''",

      // 场景4: 纯乱码/无空格 -> 原样返回
      "JustToken,             JustToken",

      // 场景5: 空字符串 -> 原样返回 (StringUtils.isNotEmpty 判断为 false)
      "'',                    ''",

      // 场景6: Null -> 返回 Null (CSV 中用 null 关键字表示 null)
      ",                      "
  })
  void getToken_ShouldHandleAllCases(String inputHeader, String expectedToken) {
    // 1. 准备请求
    MockHttpServletRequest request = new MockHttpServletRequest();
    if (inputHeader != null) {
      request.addHeader("Authorization", inputHeader);
    }

    // 2. 调用私有方法
    String result = ReflectionTestUtils.invokeMethod(tokenService, "getToken", request);

    // 3. 统一断言
    assertEquals(expectedToken, result);
  }

}