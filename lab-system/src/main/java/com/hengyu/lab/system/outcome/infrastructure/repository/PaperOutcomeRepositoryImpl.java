package com.hengyu.lab.system.outcome.infrastructure.repository;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.repository.PaperOutcomeRepository;
import com.hengyu.lab.system.outcome.domain.valobj.Author;
import com.hengyu.lab.system.outcome.infrastructure.convert.AuthorConverter;
import com.hengyu.lab.system.outcome.infrastructure.convert.OutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.OutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Repository
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PaperOutcomeRepositoryImpl implements PaperOutcomeRepository {

  private final OutcomeMapper outcomeMapper;
  private final OutcomeConverter outcomeConverter;
  private final PaperOutcomeMapper paperOutcomeMapper;
  private final PaperOutcomeConverter paperOutcomeConverter;
  private final AuthorMapper authorMapper;
  private final AuthorConverter authorConverter;

  @Override
  public void save(PaperOutcome paperOutcome) {
    OutcomePO outcomePO = outcomeConverter.toPO(paperOutcome);
    PaperOutcomePO paperOutcomePO = paperOutcomeConverter.toPO(paperOutcome);
    if (outcomePO.getId() == null) {
      outcomeMapper.insert(outcomePO);
      paperOutcomePO.setOutcomeId(outcomePO.getId());
      paperOutcomeMapper.insert(paperOutcomePO);
      DomainUtil.setIdToEntity(paperOutcome, outcomePO.getId());
    } else {
      log.info("paperOutcomePO {}", paperOutcomePO);
      outcomeMapper.updateById(outcomePO);
      paperOutcomeMapper.updateById(paperOutcomePO);
      authorMapper.deleteByOutcomeId(outcomePO.getId());
    }
    saveAuthor(paperOutcome);
  }

  @Override
  public void delete(PaperOutcome outcome) {
    OutcomePO outcomePO = outcomeConverter.toPO(outcome);
    PaperOutcomePO paperOutcomePO = paperOutcomeConverter.toPO(outcome);
    outcomeMapper.deleteById(outcomePO);
    paperOutcomeMapper.deleteById(paperOutcomePO);
  }

  @Override
  public Optional<PaperOutcome> findById(Long id) {
    PaperOutcomePO paperOutcomePO = paperOutcomeMapper.selectById(id);
    if (paperOutcomePO == null) {
      return Optional.empty();
    }
    OutcomePO outcomePO = outcomeMapper.selectById(id);
    if (outcomePO == null) {
      throw new BizException("存在严重的数据不一致，论文扩展表数据存在，却找不到主表数据");
    }
    PaperOutcome paperOutcome = paperOutcomeConverter.toDomain(outcomePO, paperOutcomePO);

    List<AuthorPO> authorPOS = authorMapper.selectAllByOutcomeId(id);
    if (!CollectionUtils.isEmpty(authorPOS)) {
      paperOutcome.assignAuthors(authorPOS.stream().map(authorConverter::toEntity).toList());
    }

    return Optional.of(paperOutcome);
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

}
