package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.system.outcome.domain.vo.Author;
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

  @Override
  public void assignCorresponding(List<Integer> authorIds) {
    this.getAuthors().forEach(author -> {
      if (authorIds.contains(author.getId())) {
        author.setIsCorresponding(Author.CORRESPONDING);
      }
    });
  }
}

