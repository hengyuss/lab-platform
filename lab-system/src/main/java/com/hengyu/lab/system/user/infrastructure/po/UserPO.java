package com.hengyu.lab.system.user.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hengyu.lab.common.persistence.BasePO;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@TableName("sys_user")
@Setter
@ToString
public class UserPO extends BasePO {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String username;

  private String email;

  private String mobile;

  private String password;

  private String realName;

  private IdentityType  identityType;



}
