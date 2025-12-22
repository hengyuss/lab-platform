package com.hengyu.lab.system.feedback.domain.exception;

import com.hengyu.lab.common.api.IResultCode;
import com.hengyu.lab.common.exception.BizException;
import lombok.Getter;

@Getter
public class FeedbackException extends BizException {

  public FeedbackException(IResultCode errorCode) {
    super(errorCode);
  }


  public FeedbackException(String msg) {
    super(msg);
  }

}
