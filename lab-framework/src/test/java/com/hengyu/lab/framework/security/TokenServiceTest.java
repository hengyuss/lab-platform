package com.hengyu.lab.framework.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.framework.redis.RedisCache;
import com.hengyu.lab.framework.utils.JwtUtils;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

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
    String token = "mock-jwt-token";
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
    when(jwtUtils.parseToken(token)).thenReturn(claims);

    AuthUser user = tokenService.getUser(request);

    verify(redisCache).getCacheObject(AuthConstants.LOGIN_TOKEN_KEY + authUser.getUniqueKey());

    Assertions.assertEquals(user.getUserId(), authUser.getUserId());
    Assertions.assertEquals(user.getUsername(), authUser.getUsername());

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

}