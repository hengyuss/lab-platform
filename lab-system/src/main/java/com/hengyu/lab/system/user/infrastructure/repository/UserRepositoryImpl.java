package com.hengyu.lab.system.user.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.repository.UserRepository;
import com.hengyu.lab.system.user.infrastructure.convert.UserConverter;
import com.hengyu.lab.system.user.infrastructure.convert.UserRoleConverter;
import com.hengyu.lab.system.user.infrastructure.mapper.UserMapper;
import com.hengyu.lab.system.user.infrastructure.mapper.UserRoleMapper;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import com.hengyu.lab.system.user.infrastructure.po.UserRolePO;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserMapper userMapper;

  private final UserConverter userConverter;

  private final UserRoleMapper userRoleMapper;

  private final UserRoleConverter roleConverter;

  @Override
  public void save(User user) {
    UserPO userPO = userConverter.toPO(user);
    if (userPO.getId() == null) {
      userMapper.insert(userPO);
      DomainUtil.setIdToEntity(user, userPO.getId());
    } else {
      userMapper.updateById(userPO);
      userRoleMapper.delete(new QueryWrapper<UserRolePO>().eq("user_id", userPO.getId()));
    }
    if (user.getRoleIds() != null && !user.getRoleIds().isEmpty()) {
      List<UserRolePO> userRolePOList = roleConverter.toPOList(user);
      userRoleMapper.insertUserRoleBatch(userRolePOList);
    }
  }

  @Override
  public Optional<User> findById(Long l) {
    UserPO userPO = userMapper.selectById(l);
    return Optional.ofNullable(userPO)
        .map(po -> {
          User user = userConverter.toDomain(po);
          LambdaQueryWrapper<UserRolePO> query = new LambdaQueryWrapper<UserRolePO>().eq(
              UserRolePO::getUserId, user.getId());

          List<Object> roleIdObjs = userRoleMapper.selectObjs(query.select(UserRolePO::getRoleId));
          List<Long> roleIdList = roleIdObjs.stream()
              .filter(Objects::nonNull)
              .map(id -> (Long) id)
              .toList();
          user.assignRoles(roleIdList);
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
