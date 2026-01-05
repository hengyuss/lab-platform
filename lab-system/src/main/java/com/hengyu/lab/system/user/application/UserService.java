package com.hengyu.lab.system.user.application;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;

  public Optional<User> findUserById(long l) {
    return userRepository.findById(l);
  }
}
