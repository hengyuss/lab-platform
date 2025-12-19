package com.hengyu.lab.system.feedback.domain.security;

public interface PasswordEncryptor {

  String encrypt(String password);

  boolean matches(String rawPassword, String encodedPassword);

}
