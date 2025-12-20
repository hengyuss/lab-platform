package com.hengyu.lab.system.feedback.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteFeedbackCmd {

  @NotBlank(message = "id不能为空")
  private String id;

}
