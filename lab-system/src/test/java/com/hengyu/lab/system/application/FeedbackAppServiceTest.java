package com.hengyu.lab.system.application;

import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.application.service.FeedbackAppService;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
import com.hengyu.lab.system.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.infrastructure.persistence.po.FeedbackPO;
import java.util.List;
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
  void should_create_feedback_success(){
    String title = "title";
    String content = "content";

    CreateFeedbackCmd cmd = new CreateFeedbackCmd(title, content);
    feedBackAppService.createFeedback(cmd);

    Mockito.verify(feedBackRepository).save(Mockito.any());
  }

  @Test
  void get_feedback_by_page(){
    FeedbackQry feedbackQry = new FeedbackQry();
    Page<FeedbackPO> mockPage = new Page<>();
    mockPage.setRecords(List.of(new FeedbackPO()));
    mockPage.setTotal(100);
    when(feedbackMapper.selectPage(Mockito.any(), Mockito.any())).thenReturn(mockPage);
    feedBackAppService.getFeedbackPage(feedbackQry);
    Mockito.verify(feedbackMapper).selectPage(Mockito.any(), Mockito.any());
    Mockito.verify(feedbackConverter).toCO(Mockito.any());
  }

}