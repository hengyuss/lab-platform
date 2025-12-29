package com.hengyu.lab.system.user.infrastructure.security;

import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  private final UserConverter converter;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(user -> converter.toAuthUser(user))
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
