package com.hengyu.lab.system.permission.infrastructure.convert;

import com.hengyu.lab.system.permission.domain.Role;
import com.hengyu.lab.system.permission.infrastructure.po.RoleMenuPO;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RoleMenuConverter {

  public List<Long> toMenuIds(List<RoleMenuPO> roleMenuPOS) {
    return roleMenuPOS.stream()
        .filter(Objects::nonNull)
        .map(RoleMenuPO::getMenuId)
        .collect(Collectors.toList());
  }



  public List<RoleMenuPO> toPOList(Role role) {
    Long roleId = role.getRoleId();
    return role.getMenuIds().stream()
        .map(menuId -> {
          RoleMenuPO roleMenuPO = new RoleMenuPO();
          roleMenuPO.setMenuId(menuId);
          roleMenuPO.setRoleId(roleId);
          return roleMenuPO;
        }).collect(Collectors.toList());
  }
}
