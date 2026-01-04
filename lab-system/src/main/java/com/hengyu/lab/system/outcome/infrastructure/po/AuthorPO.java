package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_outcome_author")
@Data
public class AuthorPO {

  @TableId(type = IdType.AUTO)
  private Integer id;
  private Long outcomeId;
  private Long userId;
  private String authorName;
  private Integer sort;
  private Integer isCorresponding;

}
