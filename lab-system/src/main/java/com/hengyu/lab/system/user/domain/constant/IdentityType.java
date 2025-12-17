package com.hengyu.lab.system.user.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentityType {
  STUDENT(1, "学生"),
  TEACHER(2, "老师");


  @EnumValue
  @JsonValue
  private final Integer TYPE;

  private final String DESC;
}
