package com.hengyu.lab.system.permission.application;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hengyu.lab.common.constant.AuthConstants;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionService {

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleService roleService;


  public Set<String> getPermission(List<Long> roleIds) {
    if (!CollectionUtils.isEmpty(roleIds) && roleIds.contains(AuthConstants.ROLE_ADMIN)) {
      return Set.of(AuthConstants.ALL_PERMISSION);
    }
    return menuService.getPermsByRoleIds(roleIds);
  }

}
