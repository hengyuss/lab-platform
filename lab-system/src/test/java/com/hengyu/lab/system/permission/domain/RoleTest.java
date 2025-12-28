package com.hengyu.lab.system.permission.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class RoleTest {

  @Test
  void assign_menuIds_success() {
    Role role = new Role();
    List<Long> menuIds =  List.of(1L, 2L);

    role.assignMenuIds(menuIds);

    Assertions.assertEquals(2, role.getMenuIds().size());
    Assertions.assertTrue(role.getMenuIds().containsAll(menuIds));

  }

}