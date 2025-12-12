package com.hengyu.lab.system.domain.feedback.repository;

import com.hengyu.lab.system.api.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.domain.feedback.Feedback;

public interface FeedbackRepository {

  void save(Feedback feedback);
}
