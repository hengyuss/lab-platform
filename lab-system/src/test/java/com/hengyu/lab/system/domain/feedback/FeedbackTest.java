package com.hengyu.lab.system.domain.feedback;

import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.domain.constant.FeedbackStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

  @ParameterizedTest(name = "当输入为 title={0}, content={1} 时，更新应返回 true")
  @CsvSource({
      "notSameTitle, testContent",      // 场景1: 只有标题变了
      "testTitle, notSameContent",      // 场景2: 只有内容变了
      "testTitle1, notSameContent1"     // 场景3: 标题和内容都变了
  })
  void update_success_when_fields_changed(String newTitle, String newContent) {
    // 1. Given: 初始状态固定
    Feedback feedback = Feedback.builder()
        .title("testTitle")
        .content("testContent")
        .build();

    // 2. When: 使用参数化提供的 newTitle 和 newContent 进行更新
    boolean updateFlag = feedback.update(newTitle, newContent);

    // 3. Then: 断言结果为 true
    Assertions.assertTrue(updateFlag);
  }


}