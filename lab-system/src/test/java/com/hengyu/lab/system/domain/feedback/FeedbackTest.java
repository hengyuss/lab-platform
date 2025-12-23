package com.hengyu.lab.system.domain.feedback;

import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.domain.constant.FeedbackStatus;
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

  @Test
  void update_feedback_same_status() {
    Feedback feedback = Feedback.builder().status(FeedbackStatus.PENDING).build();
    boolean flag = feedback.updateStatus(FeedbackStatus.PENDING);
    Assertions.assertFalse(flag);
  }

  @Test
  void update_feedback_not_same_status() {
    Feedback feedback = Feedback.builder().status(FeedbackStatus.PENDING).build();
    boolean flag = feedback.updateStatus(FeedbackStatus.SOLVING);
    Assertions.assertTrue(flag);
  }

  @Test
  void update_fail() {
    Feedback feedback = Feedback.builder().title("testTitle").content("testContent").build();
    boolean updateFlag = feedback.update("testTitle", "testContent");
    Assertions.assertFalse(updateFlag);
  }

  @Test
  void update_success_when_not_same_title() {
    Feedback feedback = Feedback.builder().title("testTitle").content("testContent").build();
    boolean updateFlag = feedback.update("notSameTitle", "testContent");
    Assertions.assertTrue(updateFlag);
  }

  @Test
  void update_success_when_not_same_content() {
    Feedback feedback = Feedback.builder().title("testTitle").content("testContent").build();
    boolean updateFlag = feedback.update("testTitle", "notSameContent");
    Assertions.assertTrue(updateFlag);
  }

  @Test
  void update_success_when_not_same_content_not_same_title() {
    Feedback feedback = Feedback.builder().title("testTitle").content("testContent").build();
    boolean updateFlag = feedback.update("testTitle1", "notSameContent1");
    Assertions.assertTrue(updateFlag);
  }


}