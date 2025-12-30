package com.hengyu.lab.common.api;

import java.util.List;
import java.util.Set;

public interface PermissionProvider {
    Set<String> getMenuPermission(List<Long> roleIds);
}
