package com.hengyu.lab.common.domain;

import com.hengyu.lab.common.annotations.TestIgnore;
import lombok.Data;

@Data
@TestIgnore
public class BasePageQuery {
  private Integer pageNo = 1;
  private Integer pageSize = 10;
}
