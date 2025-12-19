package com.hengyu.lab.system.user.domain.exception;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.common.exception.IErrorCode;
import lombok.Getter;

@Getter
public class UserException extends BizException {


  public UserException(IErrorCode errorCode) {
    super(errorCode);
  }


  public UserException(String msg) {
    super(msg);
  }


}
