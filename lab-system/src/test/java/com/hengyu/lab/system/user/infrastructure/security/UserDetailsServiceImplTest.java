package com.hengyu.lab.system.user.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserConverter userConverter;

  @Mock
  private PermissionProvider  permissionProvider;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Test
  void loadUserByUsername_success() {
    String username = "username";
    User user = User.builder()
        .identityType(IdentityType.STUDENT)
        .roleIds(List.of(1L, 2L))
        .username(username).build();
    AuthUser authUser = AuthUser.builder()
        .username(username)
        .roleIds(List.of(1L, 2L))
        .build();
    Set<String>  expectPermission = Set.of("testpermission");
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userConverter.toAuthUser(user)).thenReturn(authUser);
    when(permissionProvider.getMenuPermission(List.of(1L, 2L))).thenReturn(expectPermission);


    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

    assertEquals(username, userDetails.getUsername());
  }

  @Test
  void loadUserByUsername_fail() {
    String username = "username";
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername(username));

  }

}