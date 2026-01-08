package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;

public interface OutcomeStrategy {

  OutcomeType getOutcomeType();

  void saveDetails(Outcome outcome);

  void deleteDetails(Outcome outcome);


}
