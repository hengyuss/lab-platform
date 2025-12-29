package com.hengyu.lab.system.permission.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hengyu.lab.framework.persistence.BasePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@TableName("sys_role")
@Setter
@ToString
public class RolePO extends BasePO {
  @TableId(type = IdType.AUTO)
  private Long roleId;
  private String roleName;
  private String roleKey;
  private Integer roleSort;
  private String dataScope;
  private String status;
  private String menuCheckStrictly;
}
