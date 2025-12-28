package com.hengyu.lab.system.permission.application;

import com.hengyu.lab.system.permission.domain.Role;
import com.hengyu.lab.system.permission.domain.repository.RoleRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;
  @InjectMocks
  private RoleService roleService;

  @Test
  void save_role() {
    Role role = Role.builder()
        .roleKey("admin")
        .roleName("管理员")
        .menuIds(List.of(1L, 2L))
        .roleId(123L)
        .status("0")
        .build();

    roleService.saveRole(role);
    Mockito.verify(roleRepository).save(role);

  }
}