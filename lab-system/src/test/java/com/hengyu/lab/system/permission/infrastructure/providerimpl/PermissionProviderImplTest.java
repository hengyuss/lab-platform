package com.hengyu.lab.system.permission.infrastructure.providerimpl;

import com.hengyu.lab.system.permission.application.PermissionService;
import com.hengyu.lab.system.permission.domain.repository.RoleRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionProviderImplTest {

  @Mock
  private PermissionService permissionService;

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private PermissionProviderImpl permissionProvider;

  @Test
  void test_get_menu_permission() {
    Set<String> expect = Set.of("system:user:list", "system:role:list");
    Mockito.when(permissionService.getPermission(List.of(1L, 2L))).thenReturn(expect);
    Mockito.when(roleRepository.selectRoleIdsByUserId(1L)).thenReturn(List.of(1L, 2L));
    Set<String> menuPermission = permissionProvider.getMenuPermission(1L);
    Assertions.assertEquals(expect, menuPermission);
  }

  @Test
  void test_get_menu_role_permission_when_roleIds_is_null() {
    Set<String> menuPermission = permissionProvider.getMenuPermission(null);
    Assertions.assertEquals(Set.of(), menuPermission);
  }

}