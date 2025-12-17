package com.hengyu.lab.system.feedback.infrastructure.persistence.repository;

import com.hengyu.lab.common.utils.DomainUtil;
import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.domain.repository.FeedbackRepository;
import com.hengyu.lab.system.feedback.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.feedback.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.feedback.infrastructure.persistence.po.FeedbackPO;
import java.util.Optional;
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
    if (feedbackPO.getId() == null) {
      feedbackMapper.insert(feedbackPO);
      DomainUtil.setIdToEntity(feedback, feedbackPO.getId());
    } else {
      feedbackMapper.updateById(feedbackPO);
    }

  }

  @Override
  public Optional<Feedback> find(Long l) {
    FeedbackPO feedbackPO = feedbackMapper.selectById(l);
    return Optional.ofNullable(feedbackPO)
        .map(feedbackConverter::toDomain);
  }

  @Override
  public void removeById(Feedback feedback) {
    FeedbackPO po = feedbackConverter.toPo(feedback);
    feedbackMapper.deleteById(po);
  }

  @Override
  public void updateById(Feedback feedback) {
    FeedbackPO po = feedbackConverter.toPo(feedback);
    feedbackMapper.updateById(po);
  }
}
