package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;

public interface OutcomeStrategy {

  OutcomeType getOutcomeType();

  void setDetails(Outcome outcome);

}
