package com.hengyu.lab.system.permission.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.constant.AuthConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

  @Mock
  private RoleService roleService;

  @Mock
  private MenuService menuService;

  @InjectMocks
  private PermissionService permissionService;


  // 模拟常量值，实际代码中你应该使用真实的 AuthConstants
  private static final Long ADMIN_ROLE_ID = 1L;

  @Test
  @DisplayName("管理员场景：包含管理员角色ID，应直接返回所有权限，不查库")
  void testGetPermission_WhenIsAdmin() {
    // Arrange (准备数据)
    // 假设 AuthConstants.ROLE_ADMIN 的值是 1L
    List<Long> roleIds = Arrays.asList(ADMIN_ROLE_ID, 100L);

    // Act (执行)
    Set<String> result = permissionService.getPermission(roleIds);

    // Assert (验证)
    // 1. 验证返回值是否包含所有权限
    assertThat(result).containsExactly(AuthConstants.ALL_PERMISSION);

    // 2. 关键验证：验证 menuService 绝对没有被调用过！(性能优化验证)
    verify(menuService, never()).getPermsByRoleIds(any());
  }

  @Test
  @DisplayName("普通用户场景：不含管理员ID，应调用 MenuService 查库")
  void testGetPermission_WhenNotAdmin() {
    // Arrange
    List<Long> roleIds = Arrays.asList(2L, 3L); // 没有 1L
    Set<String> mockPerms = Set.of("user:list", "user:add");

    // 打桩：当调用 menuService 时返回模拟数据
    when(menuService.getPermsByRoleIds(roleIds)).thenReturn(mockPerms);

    // Act
    Set<String> result = permissionService.getPermission(roleIds);

    // Assert
    assertThat(result).hasSize(2).contains("user:list", "user:add");

    // 验证 menuService 确实被调用了一次
    verify(menuService, times(1)).getPermsByRoleIds(roleIds);
  }

  @Test
  @DisplayName("边界场景：角色列表为空或Null，应走普通逻辑")
  void testGetPermission_WhenEmptyOrNull() {
    // Arrange
    List<Long> emptyList = Collections.emptyList();
    when(menuService.getPermsByRoleIds(emptyList)).thenReturn(Collections.emptySet());

    // Act
    Set<String> result = permissionService.getPermission(emptyList);

    // Assert
    assertThat(result).isEmpty();
    // 确保空列表也去查了服务（或者根据你的业务逻辑，也许不需要查，看 MenuService 实现）
    verify(menuService).getPermsByRoleIds(emptyList);
  }
}