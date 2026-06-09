package com.hengyu.lab.system.outcome.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OutcomeStatus {

  DRAFT(1, "DRAFT"),
  PUBLISHED(2, "PUBLISHED"),
  PROCESSING(3, "PROCESSING"),
  ;
  @EnumValue
  @JsonValue
  Integer code;
  String desc;
  OutcomeStatus(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
