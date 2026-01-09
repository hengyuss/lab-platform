package com.hengyu.lab.system.outcome.domain.constant;

public enum OutcomeStatus {

  DRAFT(1, "DRAFT"),
  PUBLISHED(2, "PUBLISHED")
  ;
  Integer code;
  String desc;
  OutcomeStatus(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
