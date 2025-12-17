package com.hengyu.lab.system.feedback.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FeedbackStatus {
  PENDING(0, "待处理"),
  REJECTED(1, "已拒绝"),
  SOLVING(2, "处理中"),
  SOLVED(3, "已解决");

  @EnumValue
  @JsonValue
  private final Integer code;

  private final String desc;
}
