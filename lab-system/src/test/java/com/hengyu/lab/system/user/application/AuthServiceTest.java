package com.hengyu.lab.system.user.application;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.user.application.dto.command.LoginCmd;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.application.dto.vo.AuthVO;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.exception.UserException;
import com.hengyu.lab.system.user.domain.exception.UserResultCode;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import com.hengyu.lab.system.user.infrastructure.security.AuthUser;
import com.hengyu.lab.system.user.infrastructure.security.TokenService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncryptor;

  @Mock
  private UserConverter userConverter;

  @Mock
  private TokenService tokenService;

  @Mock
  private AuthenticationConfiguration authConfig;

  @Mock
  private AuthenticationManager authenticationManager;

  @InjectMocks
  private AuthService authService;


  @Test
  void register_success() {
    // --- 1. 准备数据 (Given) ---
    RegisterCmd cmd = new RegisterCmd();
    cmd.setUsername("hengyu");
    cmd.setPassword("123456"); // 必须设置，防止加密报错
    cmd.setRealName("Hengyu");

    // 模拟依赖行为
    // 1.1 模拟用户查重：不存在
    Mockito.when(userRepository.findByUsername("hengyu")).thenReturn(Optional.empty());

    // 1.2 模拟密码加密 (重要！不要漏掉)
    Mockito.when(passwordEncryptor.encode("123456")).thenReturn("encoded_123456");

    // 1.3 模拟 Converter
    AuthVO mockVo = new AuthVO();
    Mockito.when(userConverter.toAuthVO(Mockito.any(User.class))).thenReturn(mockVo);

    // 1.4 模拟 JWT 生成
    Mockito.when(tokenService.createToken(Mockito.any(AuthUser.class)))
        .thenReturn("mock-jwt-token");

    // --- 2. 执行测试 (When) ---
    AuthVO result = authService.register(cmd);

    // --- 3. 验证结果 (Then) - Assertions ---
    Assertions.assertNotNull(result);
    // 关键验证：确保 Service 层把生成的 Token 塞回去了
    Assertions.assertEquals("mock-jwt-token", result.getToken());

    // --- 4. 验证交互细节 (Then) - Verifications ---

    // 4.1 核心验证：验证存入数据库的 User 对象是否正确 (使用 ArgumentCaptor)
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    Mockito.verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    Assertions.assertEquals("hengyu", savedUser.getUsername());
    Assertions.assertEquals("encoded_123456", savedUser.getPassword()); // 验证密码是否被加密
    Assertions.assertEquals("Hengyu", savedUser.getRealName());

  }

  @Test
  void register_fail_when_username_exist() { // 1. 修正拼写
    // --- Given ---
    String username = "testUsername";
    // 模拟数据库里已经有一个人了
    User existUser = User.builder().username(username).build();
    Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(existUser));

    RegisterCmd cmd = new RegisterCmd();
    cmd.setUsername(username);
    cmd.setPassword("123456"); // 建议给上，防止空指针干扰测试逻辑

    // --- When & Then ---

    // 2. 捕获异常对象，进行更细致的断言
    UserException exception = Assertions.assertThrows(
        UserException.class,
        () -> authService.register(cmd)
    );

    // 验证错误码：确保是因为“用户名已存在”挂的，而不是别的原因
    Assertions.assertEquals(UserResultCode.USER_NAME_HAS_EXIST.getCode(), exception.getCode());

    // 3. 关键验证：确保绝对没有执行 save 操作
    // 防止业务逻辑写反了 (先保存后校验)
    Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
  }

  @Test
  void login_success() throws Exception {
    LoginCmd cmd = new LoginCmd();
    cmd.setUsername("hengyu");
    cmd.setPassword("123456");
    User mockUser = User.builder().username("hengyu").build();
    AuthUser mockAuthUser = new AuthUser(mockUser, Collections.emptyList());

    UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(
        mockAuthUser, null, mockAuthUser.getAuthorities());

    Mockito.when(authConfig.getAuthenticationManager()).thenReturn(authenticationManager);
    Mockito.when(
            authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authResult);
    Mockito.when(tokenService.createToken(Mockito.any(AuthUser.class)))
        .thenReturn("mock-jwt-token");
    Mockito.when(userConverter.toAuthVO(mockUser)).thenReturn(new AuthVO());

    AuthVO result = authService.login(cmd);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("mock-jwt-token", result.getToken());

  }

  @Test
  void login_fail_when_wrong_password() throws Exception {
    LoginCmd cmd = new LoginCmd();
    cmd.setUsername("hengyu");
    cmd.setPassword("123456");
    Mockito.when(authConfig.getAuthenticationManager()).thenReturn(authenticationManager);
    Mockito.when(
            authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Bad credentials"));

    BizException bizException = Assertions.assertThrows(BizException.class,
        () -> authService.login(cmd));
    Assertions.assertEquals(UserResultCode.USERNAME_OR_PASSWORD_ERROR.getCode(),
        bizException.getCode());
  }


}