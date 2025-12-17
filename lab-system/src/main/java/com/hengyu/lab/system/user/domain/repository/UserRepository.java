package com.hengyu.lab.system.user.domain.repository;


import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import java.util.Optional;

public interface UserRepository {

  void save(User user);

  Optional<User> findById(Long l);
}
