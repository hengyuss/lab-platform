package com.hengyu.lab.handler;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.user.domain.exception.UserResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = BizException.class)
  public R<Void> handleException(BizException e) {
    if (e.getCause() != null) {
      log.error("业务异常关联的系统错误：{}", e.getMessage(), e.getCause());
    } else {
      log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMsg());
    }
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
    log.error("系统未知异常: {}", e.getMessage());
    return R.fail(ResultCode.FAILURE.getCode(), "系统繁忙,请稍候再试");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public R<Void> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
    // 1. 打印 WARN 日志即可，因为这是用户行为（试图访问没权限的接口），不是系统 Bug
    log.warn("请求地址'{}', 权限不足: {}", request.getRequestURI(), e.getMessage());

    // 2. 返回标准的 403 状态码和提示
    return R.fail(ResultCode.NO_PRIVILEGE);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public R<Void> handleBadCredentialsException(BadCredentialsException e) {
    log.warn("登录失败：用户名或密码错误");
    // 返回业务状态码 401 或 自定义错误码
    return R.fail(UserResultCode.USERNAME_OR_PASSWORD_ERROR.getCode(), e.getMessage());
  }


}
