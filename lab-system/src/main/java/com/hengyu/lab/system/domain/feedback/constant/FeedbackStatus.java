package com.hengyu.lab.system.domain.feedback.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FeedbackStatus {
  PENDING(0, "待处理"),
  ADOPTED(1, "已采纳"),
  REJECTED(2, "已拒绝");

  @EnumValue
  private final Integer code;

  @JsonValue
  private final String desc;
}
