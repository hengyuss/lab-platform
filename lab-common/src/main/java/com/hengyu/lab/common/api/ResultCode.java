package com.hengyu.lab.common.api;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Generated
public enum ResultCode implements IResultCode {
  SUCCESS(200, "操作成功"),
  FAILURE(500, "业务异常"),
  UN_AUTHORIZED(401, "访问受限，请先登录"),
  NOT_FOUND(404, "404 没找到请求"),
  MSG_NOT_READABLE(400, "消息不能读取"),
  INTERNAL_SERVER_ERROR(500, "服务器异常"),
  ARGUMENT_NOT_VALID(405, "参数错误");;

  final Integer code;
  final String msg;

  @Override
  public Integer getCode() {
    return this.code;
  }

  @Override
  public String getMsg() {
    return this.msg;
  }
}
