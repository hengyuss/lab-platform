package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomeQry;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class PaperOutcomeStrategy implements OutcomeStrategy {

  private final PaperOutcomeConverter converter;
  private final PaperOutcomeMapper paperOutcomeMapper;


  @Override
  public OutcomeType getOutcomeType() {
    return OutcomeType.PAPER;
  }

  @Override
  public void saveDetails(Outcome outcome) {
    PaperOutcomePO po = getPaperOutcomePO(outcome);
    if (paperOutcomeMapper.selectById(po.getOutcomeId()) != null) {
      paperOutcomeMapper.updateById(po);
    } else {
      paperOutcomeMapper.insert(po);
    }
  }

  @Override
  public void deleteDetails(Outcome outcome) {
    PaperOutcomePO po = getPaperOutcomePO(outcome);
    paperOutcomeMapper.deleteById(po);
  }

  @Override
  public void buildSearchCondition(QueryWrapper<Outcome> queryWrapper, OutcomeQry query) {
    queryWrapper.eq(StringUtils.hasText(query.getIssn()), "p.issn", query.getIssn())
        .like(StringUtils.hasText(query.getJournalName()), "p.journal_name",
            query.getJournalName());
  }

  private void verifyClass(Outcome outcome) {
    if (!(outcome instanceof PaperOutcome)) {
      throw new IllegalArgumentException("类型不匹配， 期望 PaperOutcome");
    }
  }


  private PaperOutcomePO getPaperOutcomePO(Outcome outcome) {
    verifyClass(outcome);
    PaperOutcome paperOutcome = (PaperOutcome) outcome;
    PaperOutcomePO po = converter.toPO(paperOutcome);
    po.setOutcomeId(outcome.getId());
    return po;
  }

}
