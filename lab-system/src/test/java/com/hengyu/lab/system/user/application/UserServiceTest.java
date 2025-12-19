package com.hengyu.lab.system.user.application;

import com.hengyu.lab.system.feedback.domain.security.PasswordEncryptor;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.exception.UserException;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncryptor passwordEncryptor;

  @InjectMocks
  private UserService userService;


  @Test
  void resister_success(){
    Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.empty());
    RegisterCmd registerCmd = new RegisterCmd();
    registerCmd.setUsername("testUsername");
    userService.register(registerCmd);
    Mockito.verify(userRepository).save(Mockito.any(User.class));
  }

  @Test
  void resister_fail_when_username_exist(){
    User testUser = User.builder().username("testUsername").password("123456").build();
    Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(testUser));
    RegisterCmd registerCmd = new RegisterCmd();
    registerCmd.setUsername("testUsername");
    Assertions.assertThrows(UserException.class,()-> userService.register(registerCmd));
  }

}