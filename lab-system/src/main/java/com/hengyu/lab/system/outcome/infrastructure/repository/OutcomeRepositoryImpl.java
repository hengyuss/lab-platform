package com.hengyu.lab.system.outcome.infrastructure.repository;

import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.convert.AuthorConverter;
import com.hengyu.lab.system.outcome.infrastructure.convert.OutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.OutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class OutcomeRepositoryImpl implements OutcomeRepository {

  private final OutcomeMapper outcomeMapper;
  private final AuthorMapper authorMapper;
  private final OutcomeConverter outcomeConverter;
  private final AuthorConverter authorConverter;

  @Override
  public void save(Outcome outcome) {
    OutcomePO outcomePO = outcomeConverter.toPO(outcome);
    if (outcomePO.getId() == null) {
      outcomeMapper.insert(outcomePO);
      DomainUtil.setIdToEntity(outcome, outcomePO.getId());
    } else {
      outcomeMapper.updateById(outcomePO);
    }
    saveAuthor(outcome);

  }


  private void saveAuthor(Outcome outcome) {
    List<Author> authors = outcome.getAuthors();
    if (authors != null && !authors.isEmpty()) {
      List<AuthorPO> authorPOList = authorConverter.toPOList(authors).stream()
          .map(authorPO -> {
            authorPO.setOutcomeId(outcome.getId());
            return authorPO;
          })
          .toList();
      authorMapper.insert(authorPOList);
    }
  }

}
