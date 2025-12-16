package com.hengyu.lab.system.domain.feedback;

import com.hengyu.lab.common.annotations.TestIgnore;
import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

@TestIgnore
@NoArgsConstructor
@ToString
@Getter
public class Feedback {
  private Long id;
  private String title;
  private String content;
  private FeedbackStatus status;

  public Feedback(String title, String content) {
    Assert.hasText(title, "title 不能为空");
    Assert.hasText(content, "内容不能为空");
    this.title = title;
    this.content = content;
    this.status = FeedbackStatus.PENDING;
  }

  public void updateStatus(FeedbackStatus status) {
    this.status = status;
  }

}
