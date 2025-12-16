package com.hengyu.lab.system.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeedbackCmd {
  @NotBlank(message = "标题不能为空")
  public String title;
  @NotBlank(message = "内容不能为空")
  public String content;
}
