package com.hengyu.lab.common.persistence;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.hengyu.lab.common.annotations.TestIgnore;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Generated;

@Data
@TestIgnore
public class BasePO {

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;

  @TableLogic
  private Integer deleted;
}
