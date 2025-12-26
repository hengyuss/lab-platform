package com.hengyu.lab.system.user.infrastructure.security;

import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.common.redis.RedisCache;
import com.hengyu.lab.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenService {

  @Value("${lab.jwt.expiration}")
  private int expireTime;

  private final RedisCache redisCache;

  private final JwtUtils jwtUtils;

  protected static final long MILLIS_SECOND = 1000;

  protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

  private static final Long MILLIS_MINUTE_TWENTY = 20 * 60 * 1000L;

  public String createToken(AuthUser user) {
    HashMap<String, Object> tokenMap = new HashMap<>();
    tokenMap.put(AuthConstants.LOGIN_USER_ID, user.getUserId());
    tokenMap.put(AuthConstants.LOGIN_USER_ROLE, user.getIdentityType());
    String token = jwtUtils.createToken(user.getUsername(), tokenMap);
    return token;
  }

  public void refreshToken(AuthUser user) {
    user.setLoginTime(System.currentTimeMillis());
    user.setExpireTime(user.getLoginTime() + expireTime * MILLIS_MINUTE);
    String userKey = AuthConstants.LOGIN_TOKEN_KEY + user.getUserId();
    redisCache.setCacheObject(userKey, user, expireTime, TimeUnit.MINUTES);
  }


  public AuthUser getUser(HttpServletRequest request) {

    String token = getToken(request);
    if (!StringUtils.isEmpty(token)) {
      try {

        Claims claims = jwtUtils.parseToken(token);
        Long userId = (Long) claims.get(AuthConstants.LOGIN_USER_ID);
        AuthUser authUser = redisCache.getCacheObject(AuthConstants.LOGIN_TOKEN_KEY + userId);
        return authUser;
      } catch (Exception e) {
        log.error("获取用户信息失败爱  :{}", e.getMessage());
      }
    }
    return null;
  }

  private String getToken(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (StringUtils.isNotEmpty(token) && token.startsWith(AuthConstants.TOKEN_PREFIX))
    {
      token = token.replace(AuthConstants.TOKEN_PREFIX, "");
    }
    return token;
  }

  public void verifyToken(AuthUser authUser) {
    Long currentTimeMillis = System.currentTimeMillis();
    Long expireTime = authUser.getExpireTime();
    if (expireTime - currentTimeMillis < MILLIS_MINUTE_TWENTY) {
      refreshToken(authUser);
    }
  }
}
