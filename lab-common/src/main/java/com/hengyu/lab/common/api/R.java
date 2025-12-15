package com.hengyu.lab.common.api;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Generated
public class R<T> implements Serializable {
  private static final long serialVersionUID = 1L;

  private int code;       // 状态码
  private boolean success;// 是否成功
  private String msg;     // 提示消息
  private T data;         // 承载数据

  private R(IResultCode resultCode) {
    this(resultCode.getCode(), null, resultCode.getMsg());
  }

  private R(IResultCode resultCode, String msg) {
    this(resultCode.getCode(), null, msg);
  }

  private R(IResultCode resultCode, T data) {
    this(resultCode.getCode(), data, resultCode.getMsg());
  }

  private R(IResultCode resultCode, T data, String msg) {
    this(resultCode.getCode(), data, msg);
  }

  private R(int code, T data, String msg) {
    this.code = code;
    this.data = data;
    this.msg = msg;
    this.success = ResultCode.SUCCESS.code == code;
  }

  // --- 静态工厂方法，方便调用 ---

  /**
   * 成功返回，无数据
   */
  public static <T> R<T> ok() {
    return new R<>(ResultCode.SUCCESS);
  }

  /**
   * 成功返回，有数据
   */
  public static <T> R<T> ok(T data) {
    return new R<>(ResultCode.SUCCESS, data);
  }

  /**
   * 成功返回，自定义消息（一般不用，除非特殊的成功提示）
   */
  public static <T> R<T> ok(T data, String msg) {
    return new R<>(ResultCode.SUCCESS, data, msg);
  }

  /**
   * 失败返回，使用默认 500
   */
  public static <T> R<T> fail(String msg) {
    return new R<>(ResultCode.FAILURE, msg);
  }

  /**
   * 失败返回，自定义状态码
   */
  public static <T> R<T> fail(IResultCode resultCode) {
    return new R<>(resultCode);
  }

  /**
   * 失败返回，自定义状态码和消息
   */
  public static <T> R<T> fail(int code, String msg) {
    return new R<>(code, null, msg);
  }

}
