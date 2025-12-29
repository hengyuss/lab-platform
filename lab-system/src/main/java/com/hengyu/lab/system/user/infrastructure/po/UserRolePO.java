package com.hengyu.lab.system.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@TableName("sys_user_role")
@Setter
@ToString
public class UserRolePO {
  private  Long UserId;
  private Long RoleId;
}
