package com.hengyu.lab.system.application.dto.clientobject;

import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackCO {

  private String Id;
  @NotBlank(message = "标题不能为空")
  private String title;
  @NotBlank(message = "内容不能为空")
  private String content;
  @Schema(description = "反馈状态 (0:待处理, 1:已拒绝, 2:处理中, 3:已解决)", example = "0")
  private FeedbackStatus status;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

}
