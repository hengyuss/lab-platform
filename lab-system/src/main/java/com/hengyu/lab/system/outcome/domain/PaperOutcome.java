package com.hengyu.lab.system.outcome.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaperOutcome extends Outcome {
  private String journalName;
  private String issn;
  private LocalDateTime publishTime;
}

