package com.hengyu.lab.system.permission.infrastructure.convert;

import com.hengyu.lab.system.permission.domain.Role;
import com.hengyu.lab.system.permission.infrastructure.po.RolePO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface RoleConverter {


  Role toDomain(RolePO rolePO);

  RolePO toPO(Role role);
}
