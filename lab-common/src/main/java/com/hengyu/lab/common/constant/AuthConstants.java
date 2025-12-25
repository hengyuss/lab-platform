package com.hengyu.lab.common.constant;

public final class AuthConstants {

  private AuthConstants() {
    throw new AssertionError("No AuthConstants instances for you!");
  }

  public static final String HEADER = "Authorization";
  public static final String TOKEN_TYPE = "Bearer";
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String LOGIN_TOKEN_KEY = "login_tokens"; // 在redis 中的key前缀， key + userId
  public static final String LOGIN_USER_ID =  "login_user_id";
  public static final String LOGIN_USER_ROLE = "login_user_role";

}
