package com.hengyu.lab.system.infrastructure.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.hengyu.lab.common.utils.DomainUtil;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
import com.hengyu.lab.system.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.infrastructure.persistence.po.FeedbackPO;
import org.h2.schema.Domain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;



@SpringBootTest()
@ActiveProfiles("test")
@Transactional
class FeedbackRepositoryImplTest {
  @Autowired
  private FeedbackRepository feedbackRepository;

  @MockBean
  private FeedbackMapper feedbackMapper;

  @MockBean
  private FeedbackConverter feedbackConverter;


  @Test
  void save_success() {
    Feedback feedback = new Feedback("testTitle", "testContent");
    FeedbackPO feedbackPO = new FeedbackPO();
    feedbackPO.setTitle("testTitle");
    feedbackPO.setContent("testContent");

    Mockito.when(feedbackConverter.toPo(feedback)).thenReturn(feedbackPO);

    Mockito.when(feedbackMapper.insert(feedbackPO)).thenAnswer(i -> {
      FeedbackPO argument = i.getArgument(0);
      argument.setId(1L);
      return 1;
    });

    try(MockedStatic<DomainUtil> domainUtilStatic = Mockito.mockStatic(DomainUtil.class)) {
      feedbackRepository.save(feedback);
      Assertions.assertEquals(1L, feedbackPO.getId());
      Mockito.verify(feedbackConverter).toPo(feedback);
      Mockito.verify(feedbackMapper).insert((FeedbackPO) Mockito.any());
      domainUtilStatic.verify(() -> DomainUtil.setIdToEntity(feedback, feedbackPO.getId()));
    }

  }
}