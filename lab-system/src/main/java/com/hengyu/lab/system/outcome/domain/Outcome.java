package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.system.outcome.domain.constants.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import java.util.List;

public abstract class Outcome {
  private Long id;
  private String title;
  private OutcomeType type;
  private OutcomeStatus status;
  private List<Author> authors;
}
