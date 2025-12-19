package com.hengyu.lab.system.feedback.infrastructure.security;

import com.hengyu.lab.system.feedback.domain.security.PasswordEncryptor;
import org.springframework.stereotype.Component;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class BCryptPasswordEncryptor implements PasswordEncryptor {

  private final PasswordEncoder delegate = new BCryptPasswordEncoder();


  @Override
  public String encrypt(String password) {
    return delegate.encode(password);
  }

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return delegate.matches(rawPassword, encodedPassword);
  }
}
