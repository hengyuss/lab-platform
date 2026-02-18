package com.hengyu.lab.system.permission.domain.repository;


import com.hengyu.lab.system.permission.domain.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {


  void save(Role role);

  Optional<Role> findById(Long l);

  List<Role> listRole(Role role);

  List<Long> selectRoleIdsByUserId(Long userId);
}
