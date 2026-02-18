package com.hengyu.lab.system.user.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.mapper.UserMapper;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryImplTest {

  @MockBean
  private ConnectionFactory connectionFactory;

  @Autowired
  @SpyBean
  private UserRepository userRepository;

  @Autowired
  @SpyBean
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
  void save_user_userId_exist() {
    User user1 = User.builder().email("testEmail")
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .identityType(IdentityType.STUDENT)
        .build();

    userRepository.save(user1);
    Optional<User> userOptional1 = userRepository.findById(user1.getId());
    Assertions.assertTrue(userOptional1.isPresent());
    assertEquals("testMobile", userOptional1.get().getMobile());
    assertEquals("testPassword", userOptional1.get().getPassword());
    assertEquals("testRealName", userOptional1.get().getRealName());
    assertEquals("testUsername", userOptional1.get().getUsername());

    User user2 = User.builder()
        .id(user1.getId())
        .email("testEmail1")
        .mobile("testMobile1")
        .password("testPassword1")
        .realName("testRealName1")
        .username("testUsername1")
        .identityType(IdentityType.STUDENT)
        .build();

    userRepository.save(user2);
    Optional<User> userOptional2 = userRepository.findById(user1.getId());
    Assertions.assertTrue(userOptional2.isPresent());
    Assertions.assertEquals("testEmail1", userOptional2.get().getEmail());
    Assertions.assertEquals("testMobile1", userOptional2.get().getMobile());
    Mockito.verify(userMapper).updateById(Mockito.any(UserPO.class));
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