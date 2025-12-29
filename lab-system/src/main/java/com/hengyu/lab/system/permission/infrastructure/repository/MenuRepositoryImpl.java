package com.hengyu.lab.system.permission.infrastructure.repository;

import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.permission.domain.Menu;
import com.hengyu.lab.system.permission.domain.repository.MenuRepository;
import com.hengyu.lab.system.permission.infrastructure.convert.MenuConverter;
import com.hengyu.lab.system.permission.infrastructure.mapper.MenuMapper;
import com.hengyu.lab.system.permission.infrastructure.po.MenuPO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

  private final MenuMapper menuMapper;
  private final MenuConverter converter;

  @Override
  public Optional<Menu> findById(Long menuId) {
    MenuPO menuPO = menuMapper.selectById(menuId);
    return Optional.ofNullable(menuPO).map(converter::toDomain);
  }

  @Override
  public void save(Menu menu) {
    MenuPO po = converter.toPO(menu);
    if (menu.getMenuId() == null) {
      menuMapper.insert(po);
      DomainUtil.setIdToEntity(menu, po.getMenuId(), "menuId");
    } else {
      menuMapper.updateById(po);
    }
  }

  @Override
  public List<Menu> selectMenuList(Menu menu) {
    MenuPO po = converter.toPO(menu);
    return menuMapper.selectMenuList(po).stream()
        .map(converter::toDomain)
        .collect(Collectors.toList());
  }

}
