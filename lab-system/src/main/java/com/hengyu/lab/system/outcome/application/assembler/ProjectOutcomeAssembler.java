package com.hengyu.lab.system.outcome.application.assembler;

import com.hengyu.lab.system.outcome.application.command.SaveProjectOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.ProjectOutcome;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectOutcomeAssembler {

  ProjectOutcome toProjectOutcome(SaveProjectOutcomeCmd cmd);
}
