package com.hengyu.lab.system.user.application;

import com.hengyu.lab.common.constant.AuthConstants;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncryptor;
  private final UserConverter userConverter;
  private final AuthenticationConfiguration authConfig;
  private final TokenService tokenService;

  @Transactional(rollbackFor = Exception.class)
  public AuthVO register(RegisterCmd cmd) {
    Optional<User> byUsername = userRepository.findByUsername(cmd.getUsername());
    if (byUsername.isPresent()) {
      throw new UserException(UserResultCode.USER_NAME_HAS_EXIST);
    }
    String encrypt = passwordEncryptor.encode(cmd.getPassword());
    User registerUser = User.register(cmd.getUsername(), encrypt, cmd.getRealName(), cmd.getEmail(),
        cmd.getMobile(),
        cmd.getIdentityType());
    userRepository.save(registerUser);
    AuthUser authUser = new AuthUser(registerUser, Collections.emptyList());

    return buildAuthVO(authUser);
  }

  public AuthVO login(LoginCmd cmd) {
    try {
      AuthenticationManager authenticationManager = authConfig.getAuthenticationManager();
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          cmd.getUsername(), cmd.getPassword());

      Authentication authenticate = authenticationManager.authenticate(
          usernamePasswordAuthenticationToken);

      AuthUser authUser = (AuthUser) authenticate.getPrincipal();

      return buildAuthVO(authUser);
    } catch (Exception e) {
      throw new BizException(UserResultCode.USERNAME_OR_PASSWORD_ERROR);
    }
  }

  private AuthVO buildAuthVO(AuthUser user) {

    String token = tokenService.createToken(user);
    tokenService.refreshToken(user);
    AuthVO authVO = userConverter.toAuthVO(user.getUser());
    authVO.setToken(token);
    authVO.setTokenType(AuthConstants.TOKEN_TYPE);
    return authVO;
  }


}
