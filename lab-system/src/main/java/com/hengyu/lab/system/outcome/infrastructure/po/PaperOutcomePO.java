package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("sys_outcome_paper")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaperOutcomePO {
  @TableId(type = IdType.NONE)
  private Long outcomeId;
  private String journalName;
  private String issn;
  private LocalDateTime publishTime;
}
