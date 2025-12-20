package com.hengyu.lab.system.feedback.domain.exception;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.common.exception.IErrorCode;
import lombok.Getter;

@Getter
public class FeedbackException extends BizException {

  public FeedbackException(IErrorCode errorCode) {
    super(errorCode);
  }


  public FeedbackException(String msg) {
    super(msg);
  }

}
