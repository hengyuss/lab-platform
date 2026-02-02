package com.hengyu.lab.system.outcome.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

@Data
public class PaperMessage implements Serializable {

  @JsonProperty("name")
  @NotBlank(message = "老师名字不能为空")
  private String teacherName;
}
