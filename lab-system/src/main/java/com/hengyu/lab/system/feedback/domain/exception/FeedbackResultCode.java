package com.hengyu.lab.system.feedback.domain.exception;

import com.hengyu.lab.common.api.IResultCode;

public enum FeedbackResultCode implements IResultCode {
  //(0-999) 归这个异常使用的错误代码
  FEEDBACK_NOT_FOUND(0, "该需求不存在");

  private final Integer code;
  private final String msg;

  FeedbackResultCode(Integer code, String msg) {
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
