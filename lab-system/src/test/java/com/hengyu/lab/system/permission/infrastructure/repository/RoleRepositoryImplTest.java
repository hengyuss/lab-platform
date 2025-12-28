package com.hengyu.lab.system.permission.infrastructure.repository;

import com.hengyu.lab.system.permission.domain.Role;
import com.hengyu.lab.system.permission.domain.repository.RoleRepository;
import com.hengyu.lab.system.permission.infrastructure.convert.RoleMenuConverter;
import com.hengyu.lab.system.permission.infrastructure.mapper.RoleMapper;
import com.hengyu.lab.system.permission.infrastructure.mapper.RoleMenuMapper;
import com.hengyu.lab.system.permission.infrastructure.po.RolePO;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RoleRepositoryImplTest {

  @Autowired
  @SpyBean
  private RoleMapper roleMapper;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  @SpyBean
  private RoleMenuMapper roleMenuMapper;

  @Autowired
  @SpyBean
  private RoleMenuConverter roleMenuConverter;



  @Test
  void save_role_roleId_not_exist() {
    Role role = Role.builder()
        .status("0")
        .roleKey("admin")
        .roleSort(1)
        .menuIds(List.of(1L, 2L))
        .roleName("管理员")
        .build();

    roleRepository.save(role);

    Optional<Role> optionalRole = roleRepository.findById(role.getRoleId());

    Assertions.assertTrue(optionalRole.isPresent());
    Role findRole = optionalRole.get();
    Assertions.assertEquals(findRole.getMenuIds(), List.of(1L, 2L));
    Assertions.assertEquals(role.getRoleId(), findRole.getRoleId());
    Assertions.assertEquals(role.getRoleKey(), findRole.getRoleKey());
    Assertions.assertEquals(role.getRoleName(), findRole.getRoleName());
    Assertions.assertEquals(role.getStatus(), findRole.getStatus());
  }

  @Test
  void save_role_when_role_have_no_menuIds() {
    Role role = Role.builder()
        .status("0")
        .roleKey("admin")
        .roleSort(1)
        .roleName("管理员")
        .build();

    roleRepository.save(role);

    Optional<Role> optionalRole = roleRepository.findById(role.getRoleId());

    Assertions.assertTrue(optionalRole.isPresent());
    Role findRole = optionalRole.get();
    Assertions.assertTrue(findRole.getMenuIds().isEmpty());
    Mockito.verify(roleMenuConverter, Mockito.never()).toPOList(role);
    Mockito.verify(roleMenuMapper, Mockito.never()).insertRoleMenuBatch(Mockito.anyList());
  }


  @Test
  void save_role_when_role_menuIds_is_empty() {
    Role role = Role.builder()
        .status("0")
        .roleKey("admin")
        .menuIds(List.of())
        .roleSort(1)
        .roleName("管理员")
        .build();

    roleRepository.save(role);

    Optional<Role> optionalRole = roleRepository.findById(role.getRoleId());

    Assertions.assertTrue(optionalRole.isPresent());
    Role findRole = optionalRole.get();
    Assertions.assertTrue(findRole.getMenuIds().isEmpty());
    Mockito.verify(roleMenuConverter, Mockito.never()).toPOList(role);
    Mockito.verify(roleMenuMapper, Mockito.never()).insertRoleMenuBatch(Mockito.anyList());
  }




  @Test
  void save_role_roleId_exist() {
    Role role1 = Role.builder()
        .status("0")
        .roleKey("admin")
        .roleSort(1)
        .roleName("管理员")
        .build();
    roleRepository.save(role1);
    Optional<Role> optionalRole1 = roleRepository.findById(role1.getRoleId());
    Assertions.assertTrue(optionalRole1.isPresent());
    Role findRole1 = optionalRole1.get();
    Assertions.assertEquals(findRole1.getRoleKey(), "admin");
    Assertions.assertEquals(findRole1.getRoleName(), "管理员");

    Role role2 = Role.builder()
        .roleId(role1.getRoleId())
        .roleKey("common")
        .roleName("普通用户")
        .build();
    roleRepository.save(role2);
    Optional<Role> optionalRole2 = roleRepository.findById(role1.getRoleId());
    Mockito.verify(roleMapper).updateById(Mockito.any(RolePO.class));
    Assertions.assertTrue(optionalRole2.isPresent());
    Role findRole2 = optionalRole2.get();
    Assertions.assertEquals(findRole2.getRoleKey(), "common");
    Assertions.assertEquals(findRole2.getRoleName(), "普通用户");
  }


}