package com.hengyu.lab.system.permission.domain.repository;

import com.hengyu.lab.system.permission.domain.Menu;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MenuRepository {

  Optional<Menu> findById(Long menuId);

  void save(Menu menu);

  List<Menu> selectMenuList(Menu menu);

  Set<String> getPermsByRoleIds(List<Long> roleIds);
}
