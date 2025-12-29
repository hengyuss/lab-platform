package com.hengyu.lab.system.user.infrastructure.convert;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.infrastructure.po.UserRolePO;
import java.util.List;
import org.springframework.stereotype.Component;

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
