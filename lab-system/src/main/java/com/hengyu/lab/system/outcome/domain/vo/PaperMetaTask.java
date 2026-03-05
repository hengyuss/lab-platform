package com.hengyu.lab.system.outcome.domain.vo;

import lombok.Data;

@Data
public class PaperMetaTask {

  public static final String DEFAULT_TEACHER_NAME = "all";

  private String teacherName = DEFAULT_TEACHER_NAME;

  private String teacherPid;

}
