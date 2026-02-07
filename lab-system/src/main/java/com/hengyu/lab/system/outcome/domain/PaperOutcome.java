package com.hengyu.lab.system.outcome.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaperOutcome extends Outcome {
  private String journalName;
  private String issn;
  private String paperType;
  private Integer year;
  private String dblpKey;
  private LocalDateTime publishTime;
}

