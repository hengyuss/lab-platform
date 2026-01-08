package com.hengyu.lab.system.application;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.feedback.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.feedback.application.dto.command.DeleteFeedbackCmd;
import com.hengyu.lab.system.feedback.application.dto.command.UpdateFeedbackCmd;
import com.hengyu.lab.system.feedback.application.dto.command.UpdateFeedbackStatusCmd;
import com.hengyu.lab.system.feedback.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.feedback.application.service.FeedbackAppService;
import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.domain.constant.FeedbackStatus;
import com.hengyu.lab.system.feedback.domain.exception.FeedbackException;
import com.hengyu.lab.system.feedback.domain.repository.FeedbackRepository;
import com.hengyu.lab.system.feedback.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.feedback.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.feedback.infrastructure.persistence.po.FeedbackPO;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedbackAppServiceTest {


  @Mock
  private FeedbackRepository feedBackRepository;

  @Mock
  private FeedbackMapper feedbackMapper;

  @Mock
  private FeedbackConverter feedbackConverter;

  @InjectMocks
  private FeedbackAppService feedBackAppService;

  @Test
  void should_create_feedback_success() {
    String title = "title";
    String content = "content";

    CreateFeedbackCmd cmd = new CreateFeedbackCmd(title, content);
    feedBackAppService.createFeedback(cmd);

    Mockito.verify(feedBackRepository).save(Mockito.any());
  }

  @Test
  void get_feedback_by_page() {
    FeedbackQry feedbackQry = new FeedbackQry();
    Page<FeedbackPO> mockPage = new Page<>();
    mockPage.setRecords(List.of(new FeedbackPO()));
    mockPage.setTotal(100);
    when(feedbackMapper.selectPage(Mockito.any(), Mockito.any())).thenReturn(mockPage);
    feedBackAppService.getFeedbackPage(feedbackQry);
    Mockito.verify(feedbackMapper).selectPage(Mockito.any(), Mockito.any());
    Mockito.verify(feedbackConverter).toCO(Mockito.any());
  }

  @Test
  void delete_feedback_exist_by_id() {
    DeleteFeedbackCmd delCmd = new DeleteFeedbackCmd();
    delCmd.setId("123");

    Feedback feedback = new Feedback();
    when(feedBackRepository.find(123L)).thenReturn(Optional.of(feedback));
    feedBackAppService.delete(delCmd);

    Mockito.verify(feedBackRepository).find(Mockito.anyLong());
    Mockito.verify(feedBackRepository).removeById(Mockito.any(Feedback.class));
  }

  @Test
  void delete_feedback_no_exist_by_id() {
    DeleteFeedbackCmd delCmd = new DeleteFeedbackCmd();
    delCmd.setId("123");

    when(feedBackRepository.find(123L)).thenThrow(new FeedbackException("test"));
    Assertions.assertThrows(FeedbackException.class, () -> feedBackAppService.delete(delCmd));

  }

  @Test
  void update_feedback_not_same_status() {
    Feedback feedback = new Feedback("title", "content");
    when(feedBackRepository.find(1L)).thenReturn(Optional.of(feedback));
    Assertions.assertEquals(FeedbackStatus.PENDING, feedback.getStatus());
    UpdateFeedbackStatusCmd cmd = new UpdateFeedbackStatusCmd();
    cmd.setId("1");
    cmd.setStatus(FeedbackStatus.SOLVING);
    feedBackAppService.updateStatus(cmd);
    Mockito.verify(feedBackRepository).save(feedback);
  }

  @Test
  void update_feedback_same_status() {
    Feedback feedback = new Feedback("title", "content");
    when(feedBackRepository.find(1L)).thenReturn(Optional.of(feedback));
    Assertions.assertEquals(FeedbackStatus.PENDING, feedback.getStatus());
    UpdateFeedbackStatusCmd cmd = new UpdateFeedbackStatusCmd();
    cmd.setId("1");
    cmd.setStatus(FeedbackStatus.PENDING);
    feedBackAppService.updateStatus(cmd);
    Mockito.verify(feedBackRepository, Mockito.never()).save(feedback);
  }


  @Test
  void update_feedback_success() {
    UpdateFeedbackCmd cmd = new UpdateFeedbackCmd();
    cmd.setId("1");
    cmd.setTitle("title");
    cmd.setContent("content");
    Feedback feedback = new Feedback();
    when(feedBackRepository.find(1L)).thenReturn(Optional.of(feedback));

    feedBackAppService.update(cmd);

    Mockito.verify(feedBackRepository).find(Mockito.anyLong());
    Mockito.verify(feedBackRepository).updateById(Mockito.any(Feedback.class));
  }

  @Test
  void update_feedback_same_title_and_content() {
    UpdateFeedbackCmd cmd = new UpdateFeedbackCmd();
    cmd.setId("1");
    cmd.setTitle("title");
    cmd.setContent("content");
    Feedback feedback = Feedback.builder().title("title").content("content").build();
    when(feedBackRepository.find(1L)).thenReturn(Optional.of(feedback));

    feedBackAppService.update(cmd);

    Mockito.verify(feedBackRepository, never()).updateById(Mockito.any(Feedback.class));

  }


}