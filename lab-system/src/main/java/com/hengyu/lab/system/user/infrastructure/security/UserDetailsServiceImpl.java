package com.hengyu.lab.system.user.infrastructure.security;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  private final UserConverter converter;

  private final PermissionProvider permissionProvider;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(user -> {
          AuthUser authUser = converter.toAuthUser(user);
          Set<String> permissions = permissionProvider.getMenuPermission(user.getRoleIds());
          authUser.setPermissions(permissions);
          return authUser;
        })
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }

}
