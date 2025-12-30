package com.hengyu.lab.system.permission.infrastructure.providerimpl;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.system.permission.application.PermissionService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionProviderImpl implements PermissionProvider {

    private final PermissionService permissionService;

    @Override
    public Set<String> getMenuPermission(List<Long> roleIds) {
        return permissionService.getPermission(roleIds);
    }
}
