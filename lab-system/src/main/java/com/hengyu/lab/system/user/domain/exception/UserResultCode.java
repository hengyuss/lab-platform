package com.hengyu.lab.system.user.domain.exception;

import com.hengyu.lab.common.api.IResultCode;

public enum UserResultCode implements IResultCode {
  //1001-1999 归这个异常使用的错误代码
  USER_NAME_HAS_EXIST(1001, "该用户名已存在"),
  USERNAME_OR_PASSWORD_ERROR(1002, "用户名或密码错误");



  private final Integer code;
  private final String msg;

  UserResultCode(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @Override
  public Integer getCode() {
    return this.code;
  }

  @Override
  public String getMsg() {
    return this.msg;
  }
}
