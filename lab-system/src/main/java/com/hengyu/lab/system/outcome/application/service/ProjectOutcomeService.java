package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.system.outcome.application.assembler.ProjectOutcomeAssembler;
import com.hengyu.lab.system.outcome.application.command.SaveProjectOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.ProjectOutcome;
import com.hengyu.lab.system.outcome.domain.repository.ProjectOutcomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectOutcomeService {

  private final ProjectOutcomeAssembler projectOutcomeAssembler;
  private final ProjectOutcomeRepository projectOutcomeRepository;

  public Long saveOutcome(SaveProjectOutcomeCmd cmd){
    ProjectOutcome projectOutcome = projectOutcomeAssembler.toProjectOutcome(cmd);
    projectOutcomeRepository.save(projectOutcome);
    return projectOutcome.getId();
  }

}
