package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.system.outcome.domain.constants.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Outcome {
  private Long id;
  private String title;
  private OutcomeType type;
  private OutcomeStatus status;
  private List<Author> authors;
}
