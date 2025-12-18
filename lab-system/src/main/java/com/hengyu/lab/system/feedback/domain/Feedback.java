package com.hengyu.lab.system.feedback.domain;

import com.hengyu.lab.common.annotations.TestIgnore;
import com.hengyu.lab.system.feedback.domain.constant.FeedbackStatus;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

@TestIgnore
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Builder
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

  public Boolean updateStatus(FeedbackStatus status) {
    Assert.notNull(status, "需求状态不能为null");
    if (Objects.equals(status, this.status)) {
      return false;
    }
    this.status = status;
    return true;
  }

  public Boolean update(String title, String content) {
    Assert.hasText(title, "title 不能为空");
    Assert.hasText(content, "content 不能为空");
    if (Objects.equals(this.title, title) && Objects.equals(this.content, content)) {
      return false;
    }
    this.title = title;
    this.content = content;
    return true;
  }
}
