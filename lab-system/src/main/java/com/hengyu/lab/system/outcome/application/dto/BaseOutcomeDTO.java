package com.hengyu.lab.system.outcome.application.dto;

import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class BaseOutcomeDTO {
  private Long id;
  private String title;
  private OutcomeType type;
  private OutcomeStatus status;
  private List<AuthorDTO> authors = new ArrayList<>();
  private String ossPath;
}
