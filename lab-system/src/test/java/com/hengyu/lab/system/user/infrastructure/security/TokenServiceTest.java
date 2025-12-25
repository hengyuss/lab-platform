package com.hengyu.lab.system.user.infrastructure.security;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.common.redis.RedisCache;
import com.hengyu.lab.common.utils.JwtUtils;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import io.jsonwebtoken.Claims;
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
    User user = User.builder().username("testUsername")
        .id(100L)
        .identityType(IdentityType.STUDENT)
        .build();

    AuthUser authUser = new AuthUser(user, Collections.emptyList());
    String expectedToken = "mock-jwt-token";
    when(jwtUtils.createToken(eq("testUsername"), Mockito.anyMap())).thenReturn(expectedToken);

    String actualToken = tokenService.createToken(authUser);

    Assertions.assertEquals(expectedToken, actualToken);

    verify(jwtUtils).createToken(eq("testUsername"), captor.capture());
    Map<String, Object> map = captor.getValue();

    Assertions.assertEquals(100L, map.get(AuthConstants.LOGIN_USER_ID));
    Assertions.assertEquals(IdentityType.STUDENT, map.get(AuthConstants.LOGIN_USER_ROLE));

  }

  @Test
  void test_refreshToken() {
    User user = User.builder().username("testUsername")
        .id(100L)
        .identityType(IdentityType.STUDENT)
        .build();
    AuthUser authUser = new AuthUser(user, Collections.emptyList());

    String userKey = AuthConstants.LOGIN_TOKEN_KEY + user.getId();

    tokenService.refreshToken(authUser);

    verify(redisCache).setCacheObject(userKey, authUser, MOCK_EXPIRE_TIME, TimeUnit.MINUTES);
  }

  @Test
  void test_get_user(){
    String token = "mock-jwt-token";
    when(request.getHeader("Authorization")).thenReturn(token);
    User testUser = User.builder().username("testUsername")
        .id(100L)
        .build();

    DefaultClaims claims = new DefaultClaims();
    claims.setSubject("testUsername");
    claims.put(AuthConstants.LOGIN_USER_ID, 100L);

    AuthUser authUser = new AuthUser(testUser, Collections.emptyList());
    when(redisCache.getCacheObject(AuthConstants.LOGIN_TOKEN_KEY + 100L)).thenReturn(authUser);
    when(jwtUtils.parseToken(token)).thenReturn(claims);

    AuthUser user = tokenService.getUser(request);

    verify(redisCache).getCacheObject(AuthConstants.LOGIN_TOKEN_KEY + user.getUserId());

    Assertions.assertEquals(user.getUserId(), authUser.getUserId());
    Assertions.assertEquals(user.getUsername(), authUser.getUsername());

  }



}