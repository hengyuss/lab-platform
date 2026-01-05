package com.hengyu.lab.system.outcome.infrastructure.convert;

import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PaperOutcomeConverter {
    PaperOutcomePO toPO(PaperOutcome paperOutcome);
}
