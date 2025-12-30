package com.hengyu.lab.system.permission.application;

import com.hengyu.lab.system.permission.domain.repository.MenuRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;

  public Set<String> getPermsByRoleIds(List<Long> roleIds) {
    Set<String> perms = menuRepository.getPermsByRoleIds(roleIds);
    return perms;
  }
}
