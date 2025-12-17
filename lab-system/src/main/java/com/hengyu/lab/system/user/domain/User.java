package com.hengyu.lab.system.user.domain;

import com.hengyu.lab.system.user.domain.constant.IdentityType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {

  private Long id;
  private String username;
  private String password;
  private String realName;
  private String email;
  private String mobile;
  private IdentityType  identityType;

}
