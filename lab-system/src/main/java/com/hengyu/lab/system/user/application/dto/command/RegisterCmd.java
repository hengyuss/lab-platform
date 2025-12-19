package com.hengyu.lab.system.user.application.dto.command;

import com.hengyu.lab.system.user.domain.constant.IdentityType;
import lombok.Data;

@Data
public class RegisterCmd {

  private String username;
  private String password;
  private String email;
  private String realName;
  private String mobile;
  private IdentityType identityType;
}
