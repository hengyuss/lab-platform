package com.hengyu.lab.system.user.application.dto.command;

import com.hengyu.lab.system.user.domain.constant.IdentityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterCmd {


  @NotBlank(message = "用户名不能为空")
  private String username;
  @NotBlank(message = "密码不能为空")
  private String password;
  @NotBlank(message = "邮箱不能为空")
  private String email;
  @NotBlank(message = "真实姓名不能为空")
  private String realName;
  @NotBlank(message = "手机不能为空")
  private String mobile;
  @NotNull(message = "身份不能为空")
  private IdentityType identityType;
}
