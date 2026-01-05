package com.hengyu.lab.system.permission.application;

import com.hengyu.lab.system.permission.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;

  public Set<String> getPermsByRoleIds(List<Long> roleIds) {
    return menuRepository.getPermsByRoleIds(roleIds);
  }
}
