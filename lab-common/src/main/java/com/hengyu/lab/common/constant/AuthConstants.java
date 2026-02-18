package com.hengyu.lab.common.constant;

public final class AuthConstants {

  private AuthConstants() {
    throw new AssertionError("No AuthConstants instances for you!");
  }

  public static final String HEADER = "Authorization";
  public static final String TOKEN_TYPE = "Bearer";
  public static final String TOKEN_PREFIX = "Bearer ";
  // 缓存key 常量
  public static final String LOGIN_TOKEN_KEY = "login_tokens:"; // 在redis 中的key前缀， key + token
  // 用户数据库唯一id
  public static final String LOGIN_USER_ID =  "login_user_id:";
  public static final String LOGIN_USER_ROLE = "login_user_role:";
  // 该用户此次登录生成的唯一id
  public static final String LOGIN_USER_KEY = "login_user_key:";
  public static final String ALL_PERMISSION = "*:*:*";
  public static final Long ROLE_ADMIN = 1L;

}
