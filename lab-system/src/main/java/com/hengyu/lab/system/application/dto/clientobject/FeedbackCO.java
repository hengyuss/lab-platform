package com.hengyu.lab.system.application.dto.clientobject;

import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FeedbackCO {
  private String Id;
  private String title;
  private String content;
  private FeedbackStatus status;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

}
