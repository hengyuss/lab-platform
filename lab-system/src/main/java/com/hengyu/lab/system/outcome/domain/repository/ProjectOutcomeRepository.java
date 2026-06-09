package com.hengyu.lab.system.outcome.domain.repository;

import com.hengyu.lab.system.outcome.domain.ProjectOutcome;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectOutcomeRepository {

  void save(ProjectOutcome projectOutcome);

}
