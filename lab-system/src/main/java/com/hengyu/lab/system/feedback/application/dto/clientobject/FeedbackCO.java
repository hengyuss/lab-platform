package com.hengyu.lab.system.feedback.application.dto.clientobject;

import com.hengyu.lab.system.feedback.domain.constant.FeedbackStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackCO {

  private String Id;
  private String title;
  private String content;
  @Schema(description = "反馈状态 (0:待处理, 1:已拒绝, 2:处理中, 3:已解决)", example = "0")
  private FeedbackStatus status;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

}
