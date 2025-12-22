package com.hengyu.lab.system.user.application.dto.vo;

import com.hengyu.lab.system.user.domain.constant.IdentityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthVO {

  private String username;
  private String id;
  private String token;
  private IdentityType identityType;
  private String tokenType;

}
