package com.hengyu.lab.common.constant;

import com.hengyu.lab.common.annotations.TestIgnore;

@TestIgnore
public final class AuthConstants {

  private AuthConstants() {
    throw new AssertionError("No AuthConstants instances for you!");
  }

   public static final String HEADER = "Authorization";
   public static final String TOKEN_TYPE = "Bearer";
   public static final String TOKEN_PREFIX = "Bearer ";

}
