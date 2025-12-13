package com.hengyu.lab.system.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
import com.hengyu.lab.system.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.infrastructure.persistence.po.FeedbackPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackAppService {
  private final FeedbackRepository feedbackRepository;
  private final FeedbackMapper feedbackMapper;
  private final FeedbackConverter feedbackConverter;

  public Long createFeedback(CreateFeedbackCmd cmd) {
    Feedback feedback = new Feedback(cmd.getTitle(), cmd.getContent());
    feedbackRepository.save(feedback);
    return feedback.getId();
  }


  public IPage<FeedbackCO> getFeedbackPage(FeedbackQry feedbackQry) {
    Page<FeedbackPO> page = new Page<>(feedbackQry.getPageNo(),
        feedbackQry.getPageSize());
    LambdaQueryWrapper<FeedbackPO> queryWrapper = new LambdaQueryWrapper<>();
    Page<FeedbackPO> feedbackPOPage = feedbackMapper.selectPage(page, queryWrapper);
    IPage<FeedbackCO> result = feedbackPOPage.convert(feedbackConverter::toCO);
    return result;
  }
}
