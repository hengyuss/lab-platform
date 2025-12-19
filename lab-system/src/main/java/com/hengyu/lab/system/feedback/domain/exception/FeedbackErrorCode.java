package com.hengyu.lab.system.feedback.domain.exception;

import com.hengyu.lab.common.exception.IErrorCode;

public enum FeedbackErrorCode implements IErrorCode {
  //(0-999) 归这个异常使用的错误代码
  FEEDBACK_NOT_FOUND(0, "该需求不存在")
  ;

  private final Integer code;
  private final String msg;

  FeedbackErrorCode(Integer code, String msg){
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
