package com.hengyu.lab.system.outcome.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProjectType {
  GUANGXI_NSF_KEY_PROJECT(1, "广西自然科学基金重点项目"),
  ;
  @EnumValue
  @JsonValue
  Integer code;
  String desc;

  ProjectType(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
