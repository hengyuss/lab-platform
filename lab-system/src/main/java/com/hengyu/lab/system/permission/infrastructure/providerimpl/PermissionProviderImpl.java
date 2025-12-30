package com.hengyu.lab.system.permission.infrastructure.providerimpl;

import com.hengyu.lab.common.api.PermissionProvider;
import com.hengyu.lab.system.permission.application.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class PermissionProviderImpl implements PermissionProvider {

    private final PermissionService permissionService;



    @Override
    public Set<String> getMenuPermission(List<String> roleIds) {
        return permissionService.getPermission(roleIds);
    }
}
