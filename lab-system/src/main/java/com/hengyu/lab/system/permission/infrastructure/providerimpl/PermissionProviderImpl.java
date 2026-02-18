package com.hengyu.lab.system.permission.infrastructure.providerimpl;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.system.permission.application.PermissionService;
import com.hengyu.lab.system.permission.domain.repository.RoleRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionProviderImpl implements PermissionProvider {

  private final PermissionService permissionService;
  private final RoleRepository repository;

  @Override
  public Set<String> getMenuPermission(Long userId) {
    List<Long> roleIds = repository.selectRoleIdsByUserId(userId);
    Set<String> permissions = permissionService.getPermission(roleIds);
    return permissions == null ? Collections.emptySet() : permissions;
  }
}
