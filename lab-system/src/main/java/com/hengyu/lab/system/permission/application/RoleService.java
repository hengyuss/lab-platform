package com.hengyu.lab.system.permission.application;

import com.hengyu.lab.system.permission.domain.Role;
import com.hengyu.lab.system.permission.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;


  public void saveRole(Role role) {
    roleRepository.save(role);
  }
}
