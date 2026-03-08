package com.hengyu.lab.system.outcome.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.convert.AuthorConverter;
import com.hengyu.lab.system.outcome.infrastructure.convert.OutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.OutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import com.hengyu.lab.system.outcome.infrastructure.repository.strategy.OutcomeStrategy;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OutcomeRepositoryImpl implements OutcomeRepository {

  private final OutcomeMapper outcomeMapper;
  private final AuthorMapper authorMapper;
  private final OutcomeConverter outcomeConverter;
  private final AuthorConverter authorConverter;
  private final List<OutcomeStrategy> strategyList;
  private final PaperOutcomeMapper paperOutcomeMapper;
  private Map<OutcomeType, OutcomeStrategy> strategyMap;

  @PostConstruct
  public void init() {
    this.strategyMap = strategyList.stream()
        .collect(Collectors.toMap(OutcomeStrategy::getOutcomeType, Function.identity()));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(Outcome outcome) {
    OutcomePO outcomePO = outcomeConverter.toPO(outcome);
    if (outcomePO.getId() == null) {
      outcomeMapper.insert(outcomePO);
      DomainUtil.setIdToEntity(outcome, outcomePO.getId());
    } else {
      outcomeMapper.updateById(outcomePO);
      authorMapper.deleteByOutcomeId(outcome.getId());
    }
    saveAuthor(outcome);
    saveDetails(outcome);
  }

  @Override
  @Transactional
  public void delete(Outcome outcome) {
    OutcomePO outcomePO = outcomeConverter.toPO(outcome);
    OutcomeStrategy strategy = getOutcomeStrategy(outcome.getType());
    strategy.deleteDetails(outcome);
    outcomeMapper.deleteById(outcomePO);
  }

  @Override
  public Optional<Outcome> findById(Long id) {
    Outcome originOutcome = outcomeMapper.findById(id);
    return Optional.ofNullable(originOutcome).map(outcome -> {
      List<Author> authors = authorMapper.selectByOutcomeId(outcome.getId());
      outcome.setAuthors(authors);
      return outcome;
    });
  }

  @Override
  public IPage<Outcome> selectOutcomePage(OutcomePaperQry qry) {
    Page<Outcome> page = new Page<>(qry.getPageNo(), qry.getPageSize());
    QueryWrapper<Outcome> wrapper = new QueryWrapper<Outcome>();
    OutcomeStrategy outcomeStrategy = getOutcomeStrategy(OutcomeType.PAPER);
    QueryWrapper finalWrapper = outcomeStrategy.buildSearchCondition(wrapper, qry);
    return outcomeMapper.selectPageDomain(page, finalWrapper);
  }

  @Override
  public boolean existsByDblpKey(String dblpKey) {
    return Boolean.TRUE.equals(paperOutcomeMapper.existsByDblpKey(dblpKey));
  }


  private void saveAuthor(Outcome outcome) {
    List<Author> authors = outcome.getAuthors();
    if (CollectionUtils.isEmpty(authors)) {
      return;
    }
    List<AuthorPO> authorPOList = authorConverter.toPOList(authors).stream()
        .map(authorPO -> {
          authorPO.setOutcomeId(outcome.getId());
          return authorPO;
        })
        .toList();
    authorMapper.insertBatch(authorPOList);
  }

  private void saveDetails(Outcome outcome) {
    OutcomeStrategy strategy = getOutcomeStrategy(outcome.getType());
    strategy.saveDetails(outcome);
  }

  private OutcomeStrategy getOutcomeStrategy(OutcomeType type) {
    OutcomeStrategy strategy = strategyMap.get(type);
    if (strategy == null) {
      throw new BizException("未找到对应存储策略 " + type);
    }
    return strategy;
  }

}
