package com.hengyu.lab.system.permission.infrastructure.providerimpl;

import com.hengyu.lab.system.permission.application.PermissionService;
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

  @InjectMocks
  private PermissionProviderImpl permissionProvider;

  @Test
  void test_get_menu_permission() {
    Set<String> expect = Set.of("system:user:list", "system:role:list");
    Mockito.when(permissionService.getPermission(List.of(1L, 2L))).thenReturn(expect);
    Set<String> menuPermission = permissionProvider.getMenuPermission(List.of(1L, 2L));
    Assertions.assertEquals(expect, menuPermission);
  }

}