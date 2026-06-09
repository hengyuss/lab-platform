package com.hengyu.lab.system.outcome.infrastructure.repository;

import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.infrastructure.convert.OutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.OutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutcomeSaveHelper {

  private final OutcomeMapper outcomeMapper;
  private final OutcomeConverter outcomeConverter;

  @Transactional(rollbackFor = Exception.class)
  public <T extends Outcome> void executeSave(
      T outcome,
      Consumer<Long> insertSpecific,
      Runnable updateSpecific,
      Runnable postSave
  ) {

    OutcomePO outcomePO = outcomeConverter.toPO(outcome);
    if (outcomePO.getId() == null) {
      outcomeMapper.insert(outcomePO);
      DomainUtil.setIdToEntity(outcome, outcomePO.getId());
      insertSpecific.accept(outcomePO.getId());
    } else {
      outcomeMapper.updateById(outcomePO);
      updateSpecific.run();
    }
    if (postSave != null) {
      postSave.run();
    }
  }

}
