package com.hengyu.lab.common.exception;

import com.hengyu.lab.common.api.ResultCode;
import lombok.Getter;

@Getter
public class FeedbackException extends RuntimeException {

  private final int code;

  public FeedbackException(ResultCode resultCode) {
    super(resultCode.getMsg());
    this.code = resultCode.getCode();
  }

  public FeedbackException(ResultCode resultCode, String msg) {
    super(msg);
    this.code = resultCode.getCode();
  }

  public FeedbackException(String msg) {
    super(msg);
    this.code = ResultCode.FAILURE.getCode();
  }

}
