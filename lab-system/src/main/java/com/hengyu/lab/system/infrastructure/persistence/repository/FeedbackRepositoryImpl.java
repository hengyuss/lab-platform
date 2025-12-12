package com.hengyu.lab.system.infrastructure.persistence.repository;

import com.hengyu.lab.common.utils.DomainUtil;
import com.hengyu.lab.system.api.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
import com.hengyu.lab.system.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.infrastructure.persistence.po.FeedbackPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor()
public class FeedbackRepositoryImpl implements FeedbackRepository {

  private final FeedbackMapper feedbackMapper;
  private final FeedbackConverter feedbackConverter;


  @Override
  public void save(Feedback feedback) {
    FeedbackPO feedbackPO = feedbackConverter.toPo(feedback);
    if (feedbackPO == null) {
      feedbackMapper.insert(feedbackPO);
      DomainUtil.setIdToEntity(feedback, feedbackPO.getId());
    } else {
      feedbackMapper.updateById(feedbackPO);
    }
  }
}
