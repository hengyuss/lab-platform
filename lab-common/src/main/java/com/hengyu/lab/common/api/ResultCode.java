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
  NO_PRIVILEGE(403, "无访问权限"),
  NOT_FOUND(404, "404 没找到请求"),
  MSG_NOT_READABLE(400, "消息不能读取"),
  INTERNAL_SERVER_ERROR(500, "服务器异常"),
  UPLOAD_FILE_FAILE(406, "文件上传失败"),
  GET_FILE_URL_FAILE(407, "获取文件链接失败"),
  REMOVE_FILE_FAILE(408, "删除文件失败"),
  FILE_PATH_NOT_EMPTY(409, "文件上传路径不能为空"),
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
