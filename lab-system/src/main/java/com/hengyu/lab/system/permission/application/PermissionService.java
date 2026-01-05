package com.hengyu.lab.system.permission.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PermissionService {

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleService roleService;


  public Set<String> getPermission(List<Long> roleIds) {
    return menuService.getPermsByRoleIds(roleIds);
  }

}
