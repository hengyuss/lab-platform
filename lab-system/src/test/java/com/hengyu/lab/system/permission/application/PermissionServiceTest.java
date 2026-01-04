package com.hengyu.lab.system.permission.application;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

  @Mock
  private RoleService roleService;

  @Mock
  private MenuService menuService;

  @InjectMocks
  private PermissionService permissionService;

  @Test
  void getPermission() {
    permissionService.getPermission(List.of(1L, 2L));
    Mockito.verify(menuService).getPermsByRoleIds(List.of(1L, 2L));
  }

}