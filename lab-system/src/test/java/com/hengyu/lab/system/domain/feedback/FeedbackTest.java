package com.hengyu.lab.system.domain.feedback;

import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FeedbackTest {

  @Test
  void should_create_feedback_success() {
    String title = "这是测试标题";
    String content = "这是一个测试内容";

    Feedback feedBack = new Feedback(title, content);

    Assertions.assertNotNull(feedBack);
    Assertions.assertEquals(feedBack.getTitle(), title);
    Assertions.assertEquals(feedBack.getContent(), content);
    Assertions.assertEquals(feedBack.getStatus(), FeedbackStatus.PENDING);
  }




}