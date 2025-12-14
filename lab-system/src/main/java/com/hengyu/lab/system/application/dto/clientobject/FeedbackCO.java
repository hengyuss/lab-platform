package com.hengyu.lab.system.application.dto.clientobject;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FeedbackCO {
  private Long Id;
  private String title;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;

}
