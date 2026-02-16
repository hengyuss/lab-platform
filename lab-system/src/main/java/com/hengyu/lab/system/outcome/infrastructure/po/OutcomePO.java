package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hengyu.lab.framework.persistence.BasePO;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import lombok.Data;

@TableName("sys_outcome")
@Data
public class OutcomePO extends BasePO {

  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO)
  private Long id;
  private String title;
  private OutcomeType type;
  private OutcomeStatus status;
  private String createBy;
  private String updateBy;
  private String remark;
  private String ossPath;
}
