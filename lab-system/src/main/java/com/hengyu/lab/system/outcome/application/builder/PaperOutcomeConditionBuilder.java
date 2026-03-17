package com.hengyu.lab.system.outcome.application.builder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.system.outcome.application.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class PaperOutcomeConditionBuilder<T> {

  public final AuthorMapper authorMapper;


  public QueryWrapper<T> buildSearchCondition(
      QueryWrapper<T> queryWrapper,
      OutcomePaperQry query) {
    queryWrapper.eq(StringUtils.hasText(query.getIssn()), "p.issn", query.getIssn())
        .eq(Objects.nonNull(query.getPublishYear()), "p.publish_year", query.getPublishYear())
        .like(StringUtils.hasText(query.getJournalName()), "p.journal_name",
            query.getJournalName())
        .like(StringUtils.hasText(query.getTitle()), "o.title", query.getTitle());
    QueryWrapper<T> finalWrapper = buildAuthorCondition(queryWrapper,
        query);
    return finalWrapper;
  }

  private QueryWrapper<T> buildAuthorCondition(
      QueryWrapper<T> queryWrapper,
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

}
