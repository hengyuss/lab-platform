package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;

public interface OutcomeStrategy {

  OutcomeType getOutcomeType();

  void saveDetails(Outcome outcome);

  void deleteDetails(Outcome outcome);

  QueryWrapper<Outcome> buildSearchCondition(QueryWrapper<Outcome> queryWrapper,
      OutcomePaperQry query);


}
