package com.hengyu.lab.system.permission.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@TableName("sys_role_menu")
@Setter
@ToString
public class RoleMenuPO {
  private  Long roleId;
  private Long menuId;
}
