package com.hengyu.lab.system.outcome.domain.constant;

public enum OutcomeStatus {

  DRAFT(1, "草稿")
  ;
  Integer code;
  String desc;
  OutcomeStatus(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
