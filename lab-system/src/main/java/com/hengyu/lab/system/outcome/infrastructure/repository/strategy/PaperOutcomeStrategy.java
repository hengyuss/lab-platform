package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class PaperOutcomeStrategy implements OutcomeStrategy {

  private final PaperOutcomeConverter converter;
  private final PaperOutcomeMapper paperOutcomeMapper;
  private final AuthorMapper authorMapper;


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
  public QueryWrapper<Outcome> buildSearchCondition(QueryWrapper<Outcome> queryWrapper,
      OutcomePaperQry query) {
    queryWrapper.eq(StringUtils.hasText(query.getIssn()), "p.issn", query.getIssn())
        .eq(Objects.nonNull(query.getPublishYear()), "p.publish_year", query.getPublishYear())
        .like(StringUtils.hasText(query.getJournalName()), "p.journal_name",
            query.getJournalName())
        .like(StringUtils.hasText(query.getTitle()), "o.title", query.getTitle());
    QueryWrapper<Outcome> finalWrapper = buildAuthorCondition(queryWrapper, query);
    return finalWrapper;
  }

  private QueryWrapper<Outcome> buildAuthorCondition(QueryWrapper<Outcome> queryWrapper,
      OutcomePaperQry query) {
    boolean hasAuthorCondition =
        StringUtils.hasText(query.getAuthorName()) || query.getAuthorSort() != null
            || query.getIsCorrespondingAuthor() != null;
    if (hasAuthorCondition) {
      List<Long> outcomeIds = authorMapper.selectOutcomeIdByAuthorNameAndSort(
          query.getAuthorName(), query.getAuthorSort(), query.getIsCorrespondingAuthor());
      if (CollectionUtils.isEmpty(outcomeIds)) {
        queryWrapper.apply("1 = 0");
      } else {
        queryWrapper.in("o.id", outcomeIds);
      }
    }
    return queryWrapper;
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
