package com.hengyu.lab.system.user.domain;

import com.hengyu.lab.system.user.domain.constant.IdentityType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String username;
  private String password;
  private String realName;
  private String email;
  private String mobile;
  private IdentityType identityType;
  private List<Long> roleIds;


  public static User register(String username, String password, String realName, String email,
      String mobile, IdentityType identityType) {
    return User.builder().username(username).password(password).realName(realName).email(email)
        .mobile(mobile).identityType(identityType).build();
  }

  public void assignRoles(List<Long> roleIds) {
    this.roleIds = roleIds;
  }

}
