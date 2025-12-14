package com.hengyu.lab.common.domain;

import com.hengyu.lab.common.annotations.TestIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TestIgnore
public class BasePageQuery {
  @Schema(description = "页码")
  private Integer pageNo = 1;
  @Schema(description = "每页数量")
  private Integer pageSize = 10;
}
