package com.hengyu.lab.system.outcome.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Author {
  private Integer id;
  private String name;
  private Integer sort;
  private Long userId;
  private Integer isCorresponding = 0;
}
