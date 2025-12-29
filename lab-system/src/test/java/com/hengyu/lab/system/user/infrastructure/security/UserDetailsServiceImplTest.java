package com.hengyu.lab.system.user.infrastructure.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import java.util.Optional;
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

  @InjectMocks
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Test
  void loadUserByUsername_success() {
    String username = "username";
    User user = User.builder()
        .identityType(IdentityType.STUDENT)
        .username(username).build();
    AuthUser authUser = AuthUser.builder()
        .username(username)
        .build();
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    when(userConverter.toAuthUser(user)).thenReturn(authUser);

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