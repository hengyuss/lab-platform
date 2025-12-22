package com.hengyu.lab.system.user.domain.exception;

import com.hengyu.lab.common.api.IResultCode;
import com.hengyu.lab.common.exception.BizException;
import lombok.Getter;

@Getter
public class UserException extends BizException {


  public UserException(IResultCode errorCode) {
    super(errorCode);
  }


  public UserException(String msg) {
    super(msg);
  }


}
