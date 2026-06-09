package com.hengyu.lab.system.outcome.infrastructure.convert;

import com.hengyu.lab.system.outcome.domain.ProjectOutcome;
import com.hengyu.lab.system.outcome.domain.constant.ProjectType;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import com.hengyu.lab.system.outcome.infrastructure.po.ProjectOutcomePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {ProjectType.class})
public interface ProjectOutcomeConverter {

  @Mapping(source = "id", target = "outcomeId")
  ProjectOutcomePO toPO(ProjectOutcome projectOutcome);

  @Mapping(source = "outcomePO.id", target = "id")
  ProjectOutcome toDomain(OutcomePO outcomePO, ProjectOutcomePO projectOutcomePO);

}
