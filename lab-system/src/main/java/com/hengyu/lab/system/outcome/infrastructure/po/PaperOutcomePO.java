package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hengyu.lab.system.outcome.domain.valobj.Partition;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "sys_outcome_paper", autoResultMap = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaperOutcomePO {

  @TableId(type = IdType.NONE)
  private Long outcomeId;
  private String journalName;
  private String paperType;
  private String issn;
  @TableField("publish_year")
  private Integer publishYear;
  private String dblpKey;
  private String ee;
  private LocalDateTime publishTime;
  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<String> fund;
  private Partition partition;
}
