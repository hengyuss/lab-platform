package com.hengyu.lab.common.constant;

public final class AuthConstants {

  private AuthConstants() {
    throw new AssertionError("No AuthConstants instances for you!");
  }

   public final static String HEADER = "Authorization";
   public final static String TOKEN_TYPE = "Bearer ";
   public final static String TOKEN_PREFIX = "Bearer ";

}
