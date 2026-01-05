package com.hengyu.lab.system.outcome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaperOutcome extends Outcome {
  private String journalName;
  private String issn;
  private LocalDateTime publishTime;
}
