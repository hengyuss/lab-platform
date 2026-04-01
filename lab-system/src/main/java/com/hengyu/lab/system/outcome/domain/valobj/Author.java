package com.hengyu.lab.system.outcome.domain.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Author {
  public static final Integer CORRESPONDING = 1;
  public static final Integer NOT_CORRESPONDING = 0;

  private Integer id;
  private String name;
  private Integer sort;
  private Long userId;
  private Integer isCorresponding = NOT_CORRESPONDING;
}
