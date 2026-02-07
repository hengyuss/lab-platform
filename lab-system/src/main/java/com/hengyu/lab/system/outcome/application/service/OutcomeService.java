package com.hengyu.lab.system.outcome.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutcomeService {

  private final OutcomeRepository outcomeRepository;
  private final OutcomeAssembler outcomeAssembler;

  public Long saveOutcome(SavePaperOutcomeCmd cmd) {
    PaperOutcome outcome = outcomeAssembler.toPaperDomain(cmd);
    outcomeRepository.save(outcome);
    return outcome.getId();
  }

  public void deleteOutcome(Long id) {
    Outcome outcome = getOutcome(id);
    outcomeRepository.delete(outcome);
  }

  public IPage<Outcome> selectOutcomePage(OutcomePaperQry qry){
    return outcomeRepository.selectOutcomePage(qry);
  }

  private Outcome getOutcome(Long id) {
    return outcomeRepository.findById(id)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
  }

}
