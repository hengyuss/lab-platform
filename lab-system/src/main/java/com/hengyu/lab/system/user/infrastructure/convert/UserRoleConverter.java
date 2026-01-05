package com.hengyu.lab.system.user.infrastructure.convert;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.infrastructure.po.UserRolePO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRoleConverter {

  public List<UserRolePO> toPOList(User user) {
    Long userId = user.getId();
    return user.getRoleIds().stream()
        .map(roleId -> {
          UserRolePO rolePO = new UserRolePO();
          rolePO.setUserId(userId);
          rolePO.setRoleId(roleId);
          return rolePO;
        }).toList();
  }
}
