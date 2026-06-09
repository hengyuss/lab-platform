package com.hengyu.lab.system.outcome.domain;

import com.hengyu.lab.system.outcome.domain.constant.ProjectType;
import com.hengyu.lab.system.outcome.domain.valobj.ProjectIndicator;
import com.hengyu.lab.system.outcome.domain.valobj.ResponsiblePerson;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ProjectOutcome extends Outcome{

  private List<String> fund;
  private List<ResponsiblePerson> responsiblePersons;
  private ProjectType projectType;
  private LocalDate startTime;
  private LocalDate endTime;
  private ProjectIndicator projectIndicator;

}
