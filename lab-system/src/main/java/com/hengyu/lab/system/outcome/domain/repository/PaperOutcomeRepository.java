package com.hengyu.lab.system.outcome.domain.repository;

import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperOutcomeRepository {

  void save(PaperOutcome paperOutcome);

  void delete(PaperOutcome outcome);

  Optional<PaperOutcome> findById(Long id);

  boolean existsByDblpKey(String dblpKey);
}
