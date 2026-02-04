package com.hengyu.lab.system.outcome.infrastructure.mq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class PaperMetaResult {

  @JsonProperty("teacher_name")
  private String teacherName;

  private String pid;

  private Boolean status;

  private List<Map<String, Object>> data;

  private int count;


}
