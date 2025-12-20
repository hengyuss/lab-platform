package com.hengyu.lab.system.user.application;

import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.common.utils.JwtUtils;
import com.hengyu.lab.system.feedback.domain.security.PasswordEncryptor;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.application.dto.vo.RegisterVO;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.exception.UserErrorCode;
import com.hengyu.lab.system.user.domain.exception.UserException;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncryptor passwordEncryptor;
  private final UserConverter userConverter;
  private final JwtUtils jwtUtils;

  @Transactional(rollbackFor = Exception.class)
  public RegisterVO register(RegisterCmd cmd) {
    Optional<User> byUsername = userRepository.findByUsername(cmd.getUsername());
    if (byUsername.isPresent()) {
      throw new UserException(UserErrorCode.USER_NAME_HAS_EXIST);
    }
    String encrypt = passwordEncryptor.encrypt(cmd.getPassword());
    User registerUser = User.register(cmd.getUsername(), encrypt, cmd.getRealName(), cmd.getEmail(),
        cmd.getMobile(),
        cmd.getIdentityType());
    userRepository.save(registerUser);
    HashMap<String, Object> tokenMap = new HashMap<>();
    tokenMap.put("userId", registerUser.getId());
    String token = jwtUtils.createToken(registerUser.getUsername(), tokenMap);
    RegisterVO registerVO = userConverter.toRegisterVO(registerUser);
    registerVO.setToken(token);
    registerVO.setTokenType(AuthConstants.TOKEN_TYPE);
    return registerVO;
  }

}
