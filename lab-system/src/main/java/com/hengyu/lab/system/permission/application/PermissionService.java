package com.hengyu.lab.system.permission.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermissionService {

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleService roleService;




}
