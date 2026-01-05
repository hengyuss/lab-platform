package com.hengyu.lab.system.outcome.domain.constants;

public enum OutcomeType {
  PAPER(1, "论文")
  ;
  Integer code;
  String desc;
  OutcomeType(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
