package com.hengyu.lab.system.outcome.application;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
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

  private Outcome getOutcome(Long id) {
    return outcomeRepository.findById(id)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
  }

}
