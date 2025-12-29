package com.hengyu.lab.system.user.infrastructure.convert;


import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.system.user.application.dto.vo.AuthVO;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import com.hengyu.lab.system.user.infrastructure.po.UserPO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserConverter {

  UserPO toPO(User user);

  User toDomain(UserPO userPO);

  AuthVO toAuthVO(User user);

  AuthUser toAuthUser(User user);


  default Integer map(IdentityType identityType) {
    if (identityType == null) {
      return null;
    }
    return identityType.getTYPE();
  }

}
