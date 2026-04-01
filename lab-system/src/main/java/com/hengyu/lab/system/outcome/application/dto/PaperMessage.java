package com.hengyu.lab.system.outcome.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hengyu.lab.system.outcome.domain.valobj.PaperMetaTask;
import java.io.Serializable;
import lombok.Data;

@Data
public class PaperMessage implements Serializable {


  @JsonProperty("teacher_name")
  private String teacherName = PaperMetaTask.DEFAULT_TEACHER_NAME;

  @JsonProperty("teacher_pid")
  private String teacherPid;


  public String getTeacherName() {
    if (this.teacherName == null || this.teacherName.trim().isEmpty()) {
      return "all";
    }
    return this.teacherName;
  }

}
