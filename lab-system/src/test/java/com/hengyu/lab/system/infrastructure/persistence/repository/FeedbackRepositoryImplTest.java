package com.hengyu.lab.system.infrastructure.persistence.repository;

import com.hengyu.lab.common.utils.DomainUtil;
import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.domain.repository.FeedbackRepository;
import com.hengyu.lab.system.feedback.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.feedback.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.feedback.infrastructure.persistence.po.FeedbackPO;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
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
  void save_feedback_not_exist() {
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

    try (MockedStatic<DomainUtil> domainUtilStatic = Mockito.mockStatic(DomainUtil.class)) {
      feedbackRepository.save(feedback);
      Assertions.assertEquals(1L, feedbackPO.getId());
      Mockito.verify(feedbackConverter).toPo(feedback);
      Mockito.verify(feedbackMapper).insert((FeedbackPO) Mockito.any());
      domainUtilStatic.verify(() -> DomainUtil.setIdToEntity(feedback, feedbackPO.getId()));
    }
  }

  @Test
  void save_feedback_exist() {
    Feedback feedback = new Feedback("testTitle", "testContent");
    FeedbackPO feedbackPO = new FeedbackPO();
    feedbackPO.setTitle("testTitle");
    feedbackPO.setContent("testContent");
    feedbackPO.setId(1L);

    Mockito.when(feedbackConverter.toPo(feedback)).thenReturn(feedbackPO);

    feedbackRepository.save(feedback);
    Mockito.verify(feedbackMapper).updateById(Mockito.any(FeedbackPO.class));
    Mockito.verify(feedbackMapper, Mockito.never()).insert(Mockito.any(FeedbackPO.class));

  }

  @Test
  void findById_success() {
    Feedback feedback = new Feedback("testTitle", "testContent");
    FeedbackPO feedbackPO = new FeedbackPO();
    BeanUtils.copyProperties(feedback, feedbackPO);
    Mockito.when(feedbackMapper.selectById(1L)).thenReturn(feedbackPO);
    Mockito.when(feedbackConverter.toDomain(feedbackPO)).thenReturn(feedback);

    Optional<Feedback> findFeedback = feedbackRepository.find(1L);

    Assertions.assertEquals("testTitle", findFeedback.get().getTitle());
    Assertions.assertEquals("testContent", findFeedback.get().getContent());
    Mockito.verify(feedbackMapper).selectById(1L);
  }

  @Test
  void findById_fail() {

    Mockito.when(feedbackMapper.selectById(1L)).thenReturn(null);

    Optional<Feedback> findFeedback = feedbackRepository.find(1L);

    Assertions.assertFalse(findFeedback.isPresent());
    Mockito.verify(feedbackMapper).selectById(1L);
    Mockito.verify(feedbackConverter, Mockito.never()).toDomain(Mockito.any());
  }

  @Test
  void removeById_success() {
    Feedback feedback = new Feedback();
    FeedbackPO feedbackPO = new FeedbackPO();
    Mockito.when(feedbackConverter.toPo(feedback)).thenReturn(feedbackPO);
    feedbackRepository.removeById(feedback);
    Mockito.verify(feedbackMapper).deleteById(feedbackPO);
  }

  @Test
  void update() {
    Feedback feedback = new Feedback();
    FeedbackPO feedbackPO = new FeedbackPO();
    Mockito.when(feedbackConverter.toPo(feedback)).thenReturn(feedbackPO);
    feedbackRepository.updateById(feedback);
    Mockito.verify(feedbackConverter).toPo(feedback);
    Mockito.verify(feedbackMapper).updateById(feedbackPO);
  }
}