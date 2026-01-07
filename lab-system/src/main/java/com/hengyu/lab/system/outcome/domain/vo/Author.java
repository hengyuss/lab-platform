package com.hengyu.lab.system.outcome.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Author {
  private String name;
  private Integer sort;
  private Long userId;
  private Integer isCorresponding;

}
