package com.hengyu.lab.system.outcome.infrastructure.repository;

import com.hengyu.lab.system.outcome.domain.ProjectOutcome;
import com.hengyu.lab.system.outcome.domain.repository.ProjectOutcomeRepository;
import com.hengyu.lab.system.outcome.infrastructure.convert.ProjectOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.ProjectOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.ProjectOutcomePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class ProjectOutcomeRepositoryImpl implements ProjectOutcomeRepository {

  private final ProjectOutcomeConverter projectOutcomeConverter;
  private final ProjectOutcomeMapper projectOutcomeMapper;
  private final OutcomeSaveHelper outcomeSaveHelper;

  @Override
  public void save(ProjectOutcome projectOutcome) {
    ProjectOutcomePO po = projectOutcomeConverter.toPO(projectOutcome);
    outcomeSaveHelper.executeSave(
        projectOutcome,
        (outcomeId) -> {
          po.setOutcomeId(outcomeId);
          projectOutcomeMapper.insert(po);
        },
        () -> {
          projectOutcomeMapper.updateById(po);
        },
        null
    );
  }

}
