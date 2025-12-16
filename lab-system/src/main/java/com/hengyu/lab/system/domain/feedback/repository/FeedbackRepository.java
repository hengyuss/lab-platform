package com.hengyu.lab.system.domain.feedback.repository;

import com.hengyu.lab.system.domain.feedback.Feedback;

import java.util.Optional;

public interface FeedbackRepository {

  void save(Feedback feedback);

  Optional<Feedback> find(Long l);

  void removeById(Feedback feedback);

  void updateById(Feedback feedback);
}
