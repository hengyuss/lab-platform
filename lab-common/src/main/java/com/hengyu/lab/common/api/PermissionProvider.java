package com.hengyu.lab.common.api;

import java.util.Set;

public interface PermissionProvider {
    Set<String> getMenuPermission(Long userId);
}
