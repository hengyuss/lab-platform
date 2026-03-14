package com.hengyu.lab.system.outcome.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DblpTeacherDTO {

  @NotBlank(message = "老师名字不能为空")
  private String teacherName;
  @NotBlank(message = "pid 不能为空")
  private String pid;

}
