package com.hengyu.lab.system.outcome.application.dto;

import com.hengyu.lab.system.outcome.domain.constant.JournalPartition;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
  private List<String> fund;
  private JournalPartition journalPartition;
  private BigDecimal factor;

}
