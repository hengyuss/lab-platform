package com.hengyu.lab.system.user.infrastructure.convert;


import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserConverter {
  UserPO toPO(User user);

  User toDomain(UserPO userPO);

}
