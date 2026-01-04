package com.hengyu.lab.system.outcome.domain.vo;

import lombok.Data;

@Data
public class Author {
  private String name;
  private Integer sort;
  private Long userId;
  private Integer isCorresponding;

}
