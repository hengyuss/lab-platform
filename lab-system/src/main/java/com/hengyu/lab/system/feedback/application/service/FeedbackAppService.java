package com.hengyu.lab.system.feedback.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.FeedbackException;
import com.hengyu.lab.system.feedback.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.feedback.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.feedback.application.dto.command.DeleteFeedbackCmd;
import com.hengyu.lab.system.feedback.application.dto.command.UpdateFeedbackCmd;
import com.hengyu.lab.system.feedback.application.dto.command.UpdateFeedbackStatusCmd;
import com.hengyu.lab.system.feedback.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.domain.repository.FeedbackRepository;
import com.hengyu.lab.system.feedback.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.feedback.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.feedback.infrastructure.persistence.po.FeedbackPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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


  public void delete(DeleteFeedbackCmd cmd) {
    Feedback feedback = getFeedback(cmd.getId());
    feedbackRepository.removeById(feedback);
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateStatus(UpdateFeedbackStatusCmd cmd) {
    Feedback feedback = getFeedback(cmd.getId());
    feedback.updateStatus(cmd.getStatus());
    log.info("执行反馈状态变更: feedbackId={}, oldStatus={}, newStatus={}",
        feedback.getId(), feedback.getStatus(), cmd.getStatus());
    feedbackRepository.save(feedback);
  }

  public void update(UpdateFeedbackCmd cmd) {
    Feedback feedback = getFeedback(cmd.getId());
    feedback.update(cmd.getTitle(), cmd.getContent());
    log.info("执行feedback 数据变更");
    feedbackRepository.updateById(feedback);
  }

  private Feedback getFeedback(String feedbackId) {
    Long id = Long.parseLong(feedbackId);
    return feedbackRepository.find(id).orElseThrow(() -> new FeedbackException(
        ResultCode.ARGUMENT_NOT_VALID, "资源不存在"));
  }

}
