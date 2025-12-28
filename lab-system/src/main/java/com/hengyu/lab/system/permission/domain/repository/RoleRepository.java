package com.hengyu.lab.system.permission.domain.repository;


import com.hengyu.lab.system.permission.domain.Role;
import java.util.Optional;

public interface RoleRepository {


  void save(Role role);

  Optional<Role> findById(Long l);

}
