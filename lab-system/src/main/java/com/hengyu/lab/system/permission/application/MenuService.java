package com.hengyu.lab.system.permission.application;

import com.hengyu.lab.system.permission.domain.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;

}
