package com.hengyu.lab.system.outcome.infrastructure.mq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class PaperMetaResult {

  @JsonProperty("teacher_name")
  private String teacherName;

  private String pid;

  private Boolean status;

  private List<PaperItemDTO> data;

  private int count;

  @Data
  public static class PaperItemDTO{
    private String title;
    private String dblpKey;
    @JsonProperty("year")
    private String publishYear;
    private String ee;
    private String type;
    private String venue;
    private List<String> authors;
  }


}
