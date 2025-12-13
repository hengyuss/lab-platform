package com.hengyu.lab.common.domain;

import lombok.Data;

@Data
public class BasePageQuery {
  private Integer pageNo = 1;
  private Integer pageSize = 10;
}
