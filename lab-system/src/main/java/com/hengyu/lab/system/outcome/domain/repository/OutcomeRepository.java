package com.hengyu.lab.system.outcome.domain.repository;

import com.hengyu.lab.system.outcome.domain.Outcome;
import org.springframework.stereotype.Repository;

@Repository
public interface OutcomeRepository {

  void save(Outcome outcome);
}
