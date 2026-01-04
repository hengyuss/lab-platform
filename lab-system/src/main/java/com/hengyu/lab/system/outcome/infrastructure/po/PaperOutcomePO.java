package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.hengyu.lab.framework.persistence.BasePO;
import java.time.LocalDateTime;

@TableName("sys_outcome_paper")
public class PaperOutcomePO extends BasePO {
  private Long outcomeId;
  private String journalName;
  private String issn;
  private LocalDateTime publishTime;
}
