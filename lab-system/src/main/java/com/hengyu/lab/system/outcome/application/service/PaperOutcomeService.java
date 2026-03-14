package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.oss.OssTemplate;
import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.application.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
import com.hengyu.lab.system.outcome.domain.repository.PaperOutcomeRepository;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaperOutcomeService {

  private final PaperOutcomeRepository paperOutcomeRepository;
  private final OutcomeAssembler outcomeAssembler;
  private final OssTemplate ossTemplate;

  public Long saveOutcome(SavePaperOutcomeCmd cmd) {
    PaperOutcome outcome = outcomeAssembler.toPaperDomain(cmd);
    paperOutcomeRepository.save(outcome);
    return outcome.getId();
  }

  public void deleteOutcome(Long id) {
    PaperOutcome outcome = getOutcome(id);
    paperOutcomeRepository.delete(outcome);
  }

  private PaperOutcome getOutcome(Long id) {
    return paperOutcomeRepository.findById(id)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
  }


  public String uploadPaperFile(Long outComeId, InputStream inputStream, String fileName) {
    String path = ossTemplate.uploadFile(outComeId, inputStream, fileName);
    PaperOutcome outcome = paperOutcomeRepository.findById(outComeId)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    outcome.setOssPath(path);
    paperOutcomeRepository.save(outcome);
    return path;
  }

  public String getOssFileUrl(String id) {
    Outcome outcome = paperOutcomeRepository.findById(Long.valueOf(id))
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    log.info("outcome ossPath {}", outcome);
    String url = ossTemplate.getPresignedUrl(outcome.getOssPath());
    return url;
  }

  public void assignCorrespondingAuthor(Long outcomeId, List<Integer> authorIds) {
    PaperOutcome outcome = paperOutcomeRepository.findById(outcomeId)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));

    outcome.assignCorresponding(authorIds);
    paperOutcomeRepository.save(outcome);

  }


}
