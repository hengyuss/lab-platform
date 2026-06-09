package com.hengyu.lab.system.outcome.application.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hengyu.lab.system.outcome.domain.constant.ProjectType;
import com.hengyu.lab.system.outcome.domain.valobj.ProjectIndicator;
import com.hengyu.lab.system.outcome.domain.valobj.ResponsiblePerson;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class SaveProjectOutcomeCmd extends BaseSaveOutcomeCmd {

  @Schema
  private List<String> fund;
  @Schema
  private List<ResponsiblePerson> responsiblePersons;
  @Schema
  private ProjectType projectType;
  @Schema
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startTime;
  @Schema
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endTime;
  @Schema
  private ProjectIndicator projectIndicator;

}
