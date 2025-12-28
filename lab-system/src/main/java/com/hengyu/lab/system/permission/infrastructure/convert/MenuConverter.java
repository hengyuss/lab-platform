package com.hengyu.lab.system.permission.infrastructure.convert;

import com.hengyu.lab.system.permission.domain.Menu;
import com.hengyu.lab.system.permission.infrastructure.po.MenuPO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface MenuConverter {

  MenuPO toPO(Menu menu);

  Menu toDomain(MenuPO menuPO);
}
