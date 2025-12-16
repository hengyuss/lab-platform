package com.hengyu.lab.system.domain.feedback;

import com.hengyu.lab.common.annotations.TestIgnore;
import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.FeedbackException;
import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

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
    Assert.hasText(title, "title 不能为空");
    Assert.hasText(content, "内容不能为空");
    this.title = title;
    this.content = content;
    this.status = FeedbackStatus.PENDING;
  }

  public void updateStatus(FeedbackStatus status) {
    Assert.notNull(status, "需求状态不能为null");
    if (this.status == status) {
      return;
    }
    this.status = status;
  }

}
