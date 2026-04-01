package com.hengyu.lab.system.outcome.domain.valobj;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Partition {

  JOURNAL_FIRST_PARTITION(1001, "1区"),
  JOURNAL_SECOND_PARTITION(1002, "2区"),
  JOURNAL_THIRD_PARTITION(1003, "3区"),
  JOURNAL_FOURTY_PARTITION(1004, "4区"),

  CONFERENCE_CCF_A(2001, "CCF A"),
  CONFERENCE_CCF_B(2002, "CCF B"),
  CONFERENCE_CCF_C(2003, "CCF C"),
  ;

  Partition(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @EnumValue
  @JsonValue
  private final int code;
  @Getter
  private final String msg;

}
