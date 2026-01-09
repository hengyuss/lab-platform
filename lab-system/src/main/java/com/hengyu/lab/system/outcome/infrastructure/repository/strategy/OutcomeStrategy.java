package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.system.outcome.domain.query.OutcomeQry;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;

public interface OutcomeStrategy {

  OutcomeType getOutcomeType();

  void saveDetails(Outcome outcome);

  void deleteDetails(Outcome outcome);

  void buildSearchCondition(QueryWrapper<OutcomePO> queryWrapper, OutcomeQry query);


}
