package com.hengyu.lab.system.outcome.exception;

import com.hengyu.lab.common.api.IResultCode;

public enum OutcomeResultCode implements IResultCode {
  //2000-2999 归成果状态码使用
  OUTCOME_NOT_FOUND(2000, "该成果不存在")
  ;

  private final Integer code;
  private final String msg;

  OutcomeResultCode(Integer code, String msg) {
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
