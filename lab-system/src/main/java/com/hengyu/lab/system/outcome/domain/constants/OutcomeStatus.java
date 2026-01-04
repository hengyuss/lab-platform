package com.hengyu.lab.system.outcome.domain.constants;

public enum OutcomeStatus {

  ;
  Integer code;
  String desc;
  OutcomeStatus(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
