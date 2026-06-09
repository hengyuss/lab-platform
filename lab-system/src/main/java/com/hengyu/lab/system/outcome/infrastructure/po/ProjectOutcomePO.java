package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.hengyu.lab.system.outcome.domain.constant.ProjectType;
import com.hengyu.lab.system.outcome.domain.valobj.ProjectIndicator;
import com.hengyu.lab.system.outcome.domain.valobj.ResponsiblePerson;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "sys_outcome_project", autoResultMap = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectOutcomePO {

  @TableId(type = IdType.NONE)
  private Long outcomeId;
  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<String> fund;
  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<ResponsiblePerson> responsiblePersons;
  private ProjectType projectType;
  private LocalDate startTime;
  private LocalDate endTime;
  @TableField(typeHandler = JacksonTypeHandler.class)
  private ProjectIndicator projectIndicator;

}
