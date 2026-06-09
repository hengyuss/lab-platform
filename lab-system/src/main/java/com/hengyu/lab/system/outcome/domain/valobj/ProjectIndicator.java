package com.hengyu.lab.system.outcome.domain.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectIndicator {

  private Long projectId;
  private String content;
}
