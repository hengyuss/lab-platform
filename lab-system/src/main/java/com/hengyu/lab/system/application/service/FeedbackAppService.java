package com.hengyu.lab.system.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.dto.command.DeleteFeedbackCmd;
import com.hengyu.lab.system.application.dto.command.UpdateFeedbackCmd;
import com.hengyu.lab.system.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
import com.hengyu.lab.system.infrastructure.persistence.convert.FeedbackConverter;
import com.hengyu.lab.system.infrastructure.persistence.mapper.FeedbackMapper;
import com.hengyu.lab.system.infrastructure.persistence.po.FeedbackPO;
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


  public Boolean delete(DeleteFeedbackCmd delCmd) {
    Long id = Long.parseLong(delCmd.getId());
    int i = feedbackRepository.removeById(id);
    return i > 0;
  }

  @Transactional(rollbackFor = Exception.class)
  public void updateStatus(UpdateFeedbackCmd cmd) {
    Long id = Long.parseLong(cmd.getId());
    //TODO 要定义统一异常处理了
    Feedback feedback = feedbackRepository.find(id).orElseThrow(RuntimeException::new);
    feedback.updateStatus(cmd.getStatus());
    log.info("执行反馈状态变更: feedbackId={}, oldStatus={}, newStatus={}",
            id, feedback.getStatus(), cmd.getStatus());
    feedbackRepository.save(feedback);
  }
}
