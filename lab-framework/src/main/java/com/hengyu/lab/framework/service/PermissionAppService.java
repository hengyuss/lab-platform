package com.hengyu.lab.framework.service;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.common.constant.AuthConstants;
import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.framework.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;

@Service("ss")
@RequiredArgsConstructor
public class PermissionAppService {

  private final PermissionProvider permissionProvider;

  public boolean hasPermi(String permission) {
    if (StringUtils.isBlank(permission)) {
      return false;
    }
    AuthUser authUser = SecurityUtils.getAuthUser();
    if (Objects.isNull(authUser) || CollectionUtils.isEmpty(authUser.getPermissions())) {
      return false;
    }

    return hasPermissions(authUser.getPermissions(), permission);
  }

  private boolean hasPermissions(Set<String> permissions, String permission) {
    return permissions.contains(AuthConstants.ALL_PERMISSION) || permissions.contains(
        StringUtils.trim(permission));
  }

}
