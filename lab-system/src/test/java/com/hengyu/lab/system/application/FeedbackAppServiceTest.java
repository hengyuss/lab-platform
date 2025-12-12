package com.hengyu.lab.system.application;

import com.hengyu.lab.system.api.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
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

  @InjectMocks
  private FeedbackAppService feedBackAppService;

  @Test
  void should_create_feedback_success(){
    String title = "title";
    String content = "content";

    CreateFeedbackCmd cmd = new CreateFeedbackCmd(title, content);
    feedBackAppService.createFeedback(cmd);

    Mockito.verify(feedBackRepository).save(Mockito.any());
  }

}