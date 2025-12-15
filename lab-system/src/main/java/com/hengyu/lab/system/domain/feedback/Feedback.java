package com.hengyu.lab.system.domain.feedback;

import com.hengyu.lab.common.annotations.TestIgnore;
import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TestIgnore
@NoArgsConstructor
@ToString
@Data
public class Feedback {
  private Long id;
  private String title;
  private String content;
  private FeedbackStatus status;

  public Feedback(String title, String content) {
    this.title = title;
    this.content = content;
    this.status = FeedbackStatus.PENDING;
  }
  public Feedback(Long id, String title, String content) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.status = FeedbackStatus.PENDING;
  }

  public void updateStatus(FeedbackStatus status) {
    this.status = status;
  }

}
