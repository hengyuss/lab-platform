package com.hengyu.lab.common.exception;

import com.hengyu.lab.common.api.IResultCode;
import com.hengyu.lab.common.api.ResultCode;

public class BizException extends RuntimeException {

  private final Integer code;

  public BizException(IResultCode errorCode) {
    super(errorCode.getMsg());
    this.code = errorCode.getCode();
  }

  public BizException(String message) {
    super(message);
    this.code = ResultCode.FAILURE.getCode();
  }

  public BizException(Integer code, String message) {
    super(message);
    this.code = code;
  }

  public BizException(ResultCode resultCode, Exception e) {
    super(resultCode.getMsg(), e);
    this.code = resultCode.getCode();
  }

  public Integer getCode() {
    if (this.code == null) {
      return ResultCode.FAILURE.getCode();
    }
    return this.code;
  }

  public String getMsg() {
    return super.getMessage();
  }






}
