package com.hengyu.lab.handler;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = BizException.class)
  public R<Void> handleException(BizException e) {
    log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMsg());
    return R.fail(e.getCode(), e.getMessage());
  }


  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  public R<Void> handleException(MethodArgumentNotValidException e) {
    String msg = e.getBindingResult().getFieldError().getDefaultMessage();
    log.warn("参数不正确 parameter={}, msg={}", e.getBindingResult().getFieldError().getField(),
        msg);
    return R.fail(ResultCode.ARGUMENT_NOT_VALID.getCode(), msg);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  public R<Void> handleException(ConstraintViolationException e) {
    String msg = e.getMessage();
    return R.fail(ResultCode.ARGUMENT_NOT_VALID.getCode(), msg);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public R<Void> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("参数校验异常: {}", e.getMessage());
    return R.fail(ResultCode.ARGUMENT_NOT_VALID.getCode(), e.getMessage());
  }

  @ExceptionHandler(value = Exception.class)
  public R<Void> handleException(Exception e) {
    log.error("系统未知异常", e);
    return R.fail(ResultCode.FAILURE.getCode(), "系统繁忙,请稍候再试");
  }

}
