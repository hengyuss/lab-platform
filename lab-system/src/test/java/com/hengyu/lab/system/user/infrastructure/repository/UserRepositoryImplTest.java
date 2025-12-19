package com.hengyu.lab.system.user.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.mapper.UserMapper;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryImplTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserMapper userMapper;

  @Test
  void save_success() {
    User user = User.builder().email("testEmail")
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .identityType(IdentityType.STUDENT)
        .build();
    userRepository.save(user);
    Optional<User> findUser = userRepository.findById(user.getId());

    assertTrue(findUser.isPresent());
    assertEquals("testEmail", findUser.get().getEmail());
    assertEquals("testMobile", findUser.get().getMobile());
    assertEquals("testPassword", findUser.get().getPassword());
    assertEquals("testRealName", findUser.get().getRealName());
    assertEquals("testUsername", findUser.get().getUsername());
    assertEquals(IdentityType.STUDENT, findUser.get().getIdentityType());
  }


  @Test
  void findById_success() {
    User user = User.builder()
        .email("testEmail")
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .identityType(IdentityType.STUDENT)
        .build();
    userRepository.save(user);

    Optional<User> byId = userRepository.findById(user.getId());
    assertTrue(byId.isPresent());
  }

  @Test
  void save_when_user_exist() {
    User user = User.builder()
        .email("testEmail")
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .identityType(IdentityType.STUDENT)
        .build();
    userRepository.save(user);
    Optional<User> firstFindUser = userRepository.findById(user.getId());
    Assertions.assertEquals("testEmail", firstFindUser.get().getEmail());
    Assertions.assertEquals("testUsername", firstFindUser.get().getUsername());
    User modifiedUser = User.builder().id(user.getId())
        .email("modifiedEmail")
        .username("modifiedUsername")
        .build();
    userRepository.save(modifiedUser);
    Optional<User> modified = userRepository.findById(user.getId());
    Assertions.assertEquals("modifiedEmail", modified.get().getEmail());
    Assertions.assertEquals("modifiedUsername", modified.get().getUsername());
  }

  @Test
  void findByUsername_success() {
    User user = User.builder()
        .email("testEmail")
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .identityType(IdentityType.STUDENT)
        .build();
    userRepository.save(user);
    Optional<User> findUser = userRepository.findByUsername("testUsername");
    assertTrue(findUser.isPresent());
  }

}