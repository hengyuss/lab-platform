package com.hengyu.lab.system.user.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.mapper.UserMapper;
import java.util.Optional;
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

    userRepository.findById(1L);
  }

}