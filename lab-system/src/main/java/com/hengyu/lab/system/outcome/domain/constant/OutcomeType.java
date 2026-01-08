package com.hengyu.lab.system.outcome.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum OutcomeType {
  PAPER(1, "PAPER")
  ;
  Integer code;
  @EnumValue
  String desc;
  OutcomeType(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
