package com.hengyu.lab.system.outcome.domain.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.query.OutcomeQry;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface OutcomeRepository {

  void save(Outcome outcome);

  void delete(Outcome outcome);

  Optional<Outcome> findById(Long id);

  IPage<Outcome> selectOutcomePage(OutcomeQry qry);

}
