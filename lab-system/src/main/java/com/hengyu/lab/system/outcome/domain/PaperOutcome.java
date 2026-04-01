package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
import com.hengyu.lab.system.outcome.domain.valobj.Author;
import com.hengyu.lab.system.outcome.domain.valobj.Partition;
import java.time.LocalDateTime;
import java.util.List;
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
  private Integer publishYear;
  private String dblpKey;
  private String ee;
  private LocalDateTime publishTime;
  private List<String> fund;
  private Partition partition;

  @Override
  public void assignCorresponding(List<Integer> authorIds) {
    this.getAuthors().forEach(author -> {
      if (authorIds.contains(author.getId())) {
        author.setIsCorresponding(Author.CORRESPONDING);
      }
    });
  }

  public PaperOutcome assignFund(List<String> fund) {
    if (fund == null) {
      throw new BizException(OutcomeResultCode.OUTCOME_FUND_IS_NULL);
    }
    this.fund = fund;
    return this;
  }

  public PaperOutcome assignPartition(Partition partition) {
    if (partition == null) {
      throw new BizException(OutcomeResultCode.OUTCOME_FUND_IS_NULL);
    }
    this.partition = partition;
    return this;
  }

}

