package com.hengyu.lab.system.outcome.infrastructure.convert;

import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface OutcomeConverter {
  OutcomePO toPO(Outcome outcome);
}
