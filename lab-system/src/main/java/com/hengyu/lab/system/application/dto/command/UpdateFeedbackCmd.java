package com.hengyu.lab.system.application.dto.command;

import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import lombok.Data;

@Data
public class UpdateFeedbackCmd {

  String id;
  FeedbackStatus status;

}
