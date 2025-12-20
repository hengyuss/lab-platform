package com.hengyu.lab.system.user.domain.exception;

import com.hengyu.lab.common.exception.IErrorCode;

public enum UserErrorCode implements IErrorCode {
  //1000-1999 归这个异常使用的错误代码
  USER_NOT_FOUND(1000, "用户不存在"),
  USER_NAME_HAS_EXIST(1001, "该用户名已存在");

  private final Integer code;
  private final String msg;

  UserErrorCode(Integer code, String msg) {
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
