package com.hengyu.lab.system.application;

import com.hengyu.lab.system.api.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.domain.feedback.repository.FeedbackRepository;
import com.hengyu.lab.system.infrastructure.persistence.mapper.FeedbackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackAppService {
  private final FeedbackRepository feedbackRepository;
  public Long createFeedback(CreateFeedbackCmd cmd) {
    Feedback feedback = new Feedback(cmd.getTitle(), cmd.getContent());
    feedbackRepository.save(feedback);
    return feedback.getId();
  }
}
