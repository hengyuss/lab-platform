package com.hengyu.lab.system.user.infrastructure.repository;

import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserRoleConverter;
import com.hengyu.lab.system.user.infrastructure.mapper.UserMapper;
import com.hengyu.lab.system.user.infrastructure.mapper.UserRoleMapper;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserRepositoryImplTest {

  @Autowired
  @SpyBean
  private UserRepository userRepository;

  @Autowired
  @SpyBean
  private UserMapper userMapper;

  @Autowired
  @SpyBean
  private UserRoleMapper userRoleMapper;

  @Autowired
  @SpyBean
  private UserRoleConverter userRoleConverter;

  @Test
  void save_success() {
    User user = User.builder().email("testEmail")
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .roleIds(List.of(1L, 2L))
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
    assertEquals(List.of(1L, 2L), findUser.get().getRoleIds());
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
    Mockito.verify(userRoleMapper).delete(Mockito.any());
  }

  @Test
  void save_user_when_user_have_no_roleIds() {
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
    assertTrue(findUser.get().getRoleIds().isEmpty());
    assertEquals("testEmail", findUser.get().getEmail());
    assertEquals("testMobile", findUser.get().getMobile());
    assertEquals("testPassword", findUser.get().getPassword());
    assertEquals("testRealName", findUser.get().getRealName());
    assertEquals("testUsername", findUser.get().getUsername());
    Mockito.verify(userRoleConverter, Mockito.never()).toPOList(user);
    Mockito.verify(userRoleMapper, Mockito.never()).insertUserRoleBatch(Mockito.anyList());
  }

  @Test
  void save_user_when_user_roleIds_is_empty(){
    User user = User.builder().email("testEmail")
        .roleIds(List.of())
        .mobile("testMobile")
        .password("testPassword")
        .realName("testRealName")
        .username("testUsername")
        .identityType(IdentityType.STUDENT)
        .build();

    userRepository.save(user);
    Optional<User> userOption = userRepository.findById(user.getId());
    assertTrue(userOption.isPresent());
    Assertions.assertTrue(userOption.get().getRoleIds().isEmpty());
    Mockito.verify(userRoleMapper, Mockito.never()).insertUserRoleBatch(Mockito.anyList());
    Mockito.verify(userRoleConverter, Mockito.never()).toPOList(Mockito.any());
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