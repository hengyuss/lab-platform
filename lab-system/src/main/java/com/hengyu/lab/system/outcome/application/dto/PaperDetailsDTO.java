package com.hengyu.lab.system.outcome.application.dto;

import com.hengyu.lab.system.outcome.domain.valobj.JournalPartition;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class PaperDetailsDTO {
  @NotNull(message = "论文ID不能为空")
  private Long outcomeId;

  private List<String> fund;

  private JournalPartition journalPartition;

  private BigDecimal factor;
}
