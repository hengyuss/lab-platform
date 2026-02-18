package com.hengyu.lab.system.user.infrastructure.repository;

import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import com.hengyu.lab.system.user.infrastructure.mapper.UserMapper;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserMapper userMapper;

  private final UserConverter userConverter;


  @Override
  public void save(User user) {
    UserPO userPO = userConverter.toPO(user);
    if (userPO.getId() == null) {
      userMapper.insert(userPO);
      DomainUtil.setIdToEntity(user, userPO.getId());
    } else {
      userMapper.updateById(userPO);
    }

  }

  @Override
  public Optional<User> findById(Long l) {
    UserPO userPO = userMapper.selectById(l);
    return Optional.ofNullable(userPO)
        .map(po -> {
          User user = userConverter.toDomain(po);
          return user;
        });
  }

  @Override
  public Optional<User> findByUsername(String username) {
    UserPO userPO = userMapper.findByUsername(username);
    return Optional.ofNullable(userPO)
        .map(userConverter::toDomain);
  }
}
