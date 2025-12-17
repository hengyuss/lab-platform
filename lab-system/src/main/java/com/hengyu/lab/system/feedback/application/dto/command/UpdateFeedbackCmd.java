package com.hengyu.lab.system.feedback.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateFeedbackCmd {
  @NotBlank(message = "id 不能为空")
  private String id;
  @NotBlank(message = "title 不能为空")
  private String title;
  @NotBlank(message = "content 不能为空")
  private String content;
}
