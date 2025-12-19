package com.hengyu.lab.system.user.application;

import com.hengyu.lab.system.feedback.domain.security.PasswordEncryptor;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.exception.UserErrorCode;
import com.hengyu.lab.system.user.domain.exception.UserException;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncryptor passwordEncryptor;

  public void register(RegisterCmd cmd) {
    String encrypt = passwordEncryptor.encrypt(cmd.getPassword());
    Optional<User> byUsername = userRepository.findByUsername(cmd.getUsername());
    if (byUsername.isPresent()) {
      throw new UserException(UserErrorCode.USER_NAME_HAS_EXIST);
    }
    User register = User.register(cmd.getUsername(), encrypt, cmd.getRealName(), cmd.getEmail(),
        cmd.getMobile(),
        cmd.getIdentityType());
    userRepository.save(register);
  }

}
