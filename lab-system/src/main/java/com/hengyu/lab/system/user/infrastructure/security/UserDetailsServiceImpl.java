package com.hengyu.lab.system.user.infrastructure.security;

import com.hengyu.lab.system.user.domain.repository.UserRepository;
import java.util.Collections;
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

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
        .map(user -> new AuthUser(user, Collections.emptyList()))
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
