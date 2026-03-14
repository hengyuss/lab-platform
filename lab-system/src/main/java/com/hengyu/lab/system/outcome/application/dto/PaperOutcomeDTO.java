package com.hengyu.lab.system.outcome.application.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaperOutcomeDTO extends BaseOutcomeDTO {

  private String journalName;
  private String issn;
  private String paperType;
  private Integer publishYear;
  private String dblpKey;
  private String ee;
  private LocalDateTime publishTime;

}
