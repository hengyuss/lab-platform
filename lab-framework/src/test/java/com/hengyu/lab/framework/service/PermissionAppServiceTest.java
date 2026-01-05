package com.hengyu.lab.framework.service;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.framework.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class PermissionAppServiceTest {

  // 1. Mock 掉 Service 的依赖 (虽然这个方法里没用到，但构造函数需要)
  @Mock
  private PermissionProvider permissionProvider;

  // 2. 注入被测对象
  @InjectMocks
  private PermissionAppService permissionAppService;

  // 3. 定义一个静态 Mock 对象
  private MockedStatic<SecurityUtils> securityUtilsMock;

  @BeforeEach
  void setUp() {
    // 在每个测试开始前，开启静态 Mock
    securityUtilsMock = mockStatic(SecurityUtils.class);
  }

  @AfterEach
  void tearDown() {
    // ⚠️ 非常重要：测试结束后必须关闭静态 Mock，否则会影响其他测试类
    securityUtilsMock.close();
  }

  @ParameterizedTest
  @DisplayName("测试：当入参为空时，直接返回 false")
  @NullAndEmptySource // 自动测试 null 和 ""
  @ValueSource(strings = {"  "}) // 测试空格
  void hasPermi_EmptyInput_ReturnsFalse(String inputPermission) {
    // Act
    boolean result = permissionAppService.hasPermi(inputPermission);

    // Assert
    assertFalse(result);
    // 验证：此时甚至不应该去获取当前用户，流程应直接中断
    securityUtilsMock.verifyNoInteractions();
  }

  @Test
  @DisplayName("测试：未登录(AuthUser为null)时，返回 false")
  void hasPermi_NoLogin_ReturnsFalse() {
    // Arrange
    securityUtilsMock.when(SecurityUtils::getAuthUser).thenReturn(null);

    // Act
    boolean result = permissionAppService.hasPermi("system:user:list");

    // Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("测试：用户已登录但没有任何权限，返回 false")
  void hasPermi_UserHasNoPerms_ReturnsFalse() {
    // Arrange
    AuthUser authUser = new AuthUser();
    authUser.setPermissions(Collections.emptySet()); // 空权限集合

    securityUtilsMock.when(SecurityUtils::getAuthUser).thenReturn(authUser);

    // Act
    boolean result = permissionAppService.hasPermi("system:user:list");

    // Assert
    assertFalse(result);
  }

  @Test
  @DisplayName("测试：超级管理员(*:*:*)，应该对任何权限返回 true")
  void hasPermi_SuperAdmin_ReturnsTrue() {
    // Arrange
    AuthUser authUser = new AuthUser();
    // 模拟拥有所有权限
    authUser.setPermissions(Set.of(AuthConstants.ALL_PERMISSION));

    securityUtilsMock.when(SecurityUtils::getAuthUser).thenReturn(authUser);

    // Act
    boolean result = permissionAppService.hasPermi("system:user:anything");

    // Assert
    assertTrue(result);
  }

  @Test
  @DisplayName("测试：精准匹配权限，返回 true")
  void hasPermi_ExactMatch_ReturnsTrue() {
    // Arrange
    String targetPerm = "system:user:list";
    AuthUser authUser = new AuthUser();
    authUser.setPermissions(Set.of("other:perm", targetPerm));

    securityUtilsMock.when(SecurityUtils::getAuthUser).thenReturn(authUser);

    // Act
    boolean result = permissionAppService.hasPermi(targetPerm);

    // Assert
    assertTrue(result);
  }

  @Test
  @DisplayName("测试：权限去空格匹配 (Trim逻辑测试)")
  void hasPermi_TrimMatch_ReturnsTrue() {
    // Arrange
    String targetPerm = "system:user:list";
    AuthUser authUser = new AuthUser();
    authUser.setPermissions(Set.of(targetPerm));

    securityUtilsMock.when(SecurityUtils::getAuthUser).thenReturn(authUser);

    // Act: 传入带空格的权限字符串
    boolean result = permissionAppService.hasPermi("  system:user:list  ");

    // Assert
    assertTrue(result);
  }

  @Test
  @DisplayName("测试：无匹配权限，返回 false")
  void hasPermi_NoMatch_ReturnsFalse() {
    // Arrange
    AuthUser authUser = new AuthUser();
    authUser.setPermissions(Set.of("system:user:list"));

    securityUtilsMock.when(SecurityUtils::getAuthUser).thenReturn(authUser);

    // Act: 问一个他没有的权限
    boolean result = permissionAppService.hasPermi("system:user:delete");

    // Assert
    assertFalse(result);
  }


}