package com.hengyu.lab.system.feedback.application.dto.command;

import com.hengyu.lab.system.feedback.domain.constant.FeedbackStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateFeedbackStatusCmd {

  @NotBlank(message = "id不能为空")
  String id;
  @NotNull(message = "状态不能为空")
  FeedbackStatus status;

}
