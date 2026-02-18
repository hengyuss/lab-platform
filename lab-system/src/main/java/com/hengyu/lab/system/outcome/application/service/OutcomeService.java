package com.hengyu.lab.system.outcome.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.oss.OssTemplate;
import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutcomeService {

  private final OutcomeRepository outcomeRepository;
  private final OutcomeAssembler outcomeAssembler;
  private final OssTemplate ossTemplate;

  public Long saveOutcome(SavePaperOutcomeCmd cmd) {
    PaperOutcome outcome = outcomeAssembler.toPaperDomain(cmd);
    outcomeRepository.save(outcome);
    return outcome.getId();
  }

  public void deleteOutcome(Long id) {
    Outcome outcome = getOutcome(id);
    outcomeRepository.delete(outcome);
  }

  public IPage<Outcome> selectOutcomePage(OutcomePaperQry qry) {
    return outcomeRepository.selectOutcomePage(qry);
  }

  private Outcome getOutcome(Long id) {
    return outcomeRepository.findById(id)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
  }

  public String uploadPaperFile(Long outComeId, InputStream inputStream, String fileName) {

    String path = ossTemplate.uploadFile(outComeId, inputStream, fileName);
    Outcome outcome = outcomeRepository.findById(outComeId)
        .orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    outcome.setOssPath(path);
    outcomeRepository.save(outcome);
    return path;
  }

  public String getOssFileUrl(String id) {
    Outcome outcome = outcomeRepository.findById(Long.valueOf(id)).orElseThrow(() -> new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    log.info("outcome ossPath {}", outcome);
    String url = ossTemplate.getPresignedUrl(outcome.getOssPath());
    return url;
  }

}
