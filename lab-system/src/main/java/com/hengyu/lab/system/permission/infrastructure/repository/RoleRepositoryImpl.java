package com.hengyu.lab.system.permission.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hengyu.lab.framework.utils.DomainUtil;
import com.hengyu.lab.system.permission.domain.Role;
import com.hengyu.lab.system.permission.domain.repository.RoleRepository;
import com.hengyu.lab.system.permission.infrastructure.convert.RoleConverter;
import com.hengyu.lab.system.permission.infrastructure.convert.RoleMenuConverter;
import com.hengyu.lab.system.permission.infrastructure.mapper.RoleMapper;
import com.hengyu.lab.system.permission.infrastructure.mapper.RoleMenuMapper;
import com.hengyu.lab.system.permission.infrastructure.po.RoleMenuPO;
import com.hengyu.lab.system.permission.infrastructure.po.RolePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

  private final RoleMapper roleMapper;
  private final RoleConverter converter;
  private final RoleMenuMapper roleMenuMapper;
  private final RoleMenuConverter roleMenuConverter;


  @Override
  @Transactional(rollbackFor = Exception.class)
  public void save(Role role) {
    RolePO po = converter.toPO(role);
    if (po.getRoleId() == null) {
      roleMapper.insert(po);
      DomainUtil.setIdToEntity(role, po.getRoleId(), "roleId");
    } else {
      roleMapper.updateById(po);
      roleMenuMapper.delete(new QueryWrapper<RoleMenuPO>().eq("role_id", po.getRoleId()));
    }
    if (role.getMenuIds() != null && !role.getMenuIds().isEmpty()) {
      List<RoleMenuPO> roleMenuPos = roleMenuConverter.toPOList(role);
      roleMenuMapper.insertRoleMenuBatch(roleMenuPos);
    }
  }

  @Override
  public Optional<Role> findById(Long l) {
    RolePO rolePO = roleMapper.selectById(l);

    return Optional.ofNullable(rolePO)
        .map(po -> {
          Role role = converter.toDomain(po);
          LambdaQueryWrapper<RoleMenuPO> query = new LambdaQueryWrapper<RoleMenuPO>().eq(
              RoleMenuPO::getRoleId, rolePO.getRoleId());

          List<Object> menuIdObjs = roleMenuMapper.selectObjs(query.select(RoleMenuPO::getMenuId));
          List<Long> menuIds = menuIdObjs.stream()
              .filter(Objects::nonNull)
              .map(id -> (Long) id)
              .collect(Collectors.toList());

          role.setMenuIds(menuIds);
          return role;
        });
  }

  @Override
  public List<Role> listRole(Role role) {
    RolePO po = converter.toPO(role);
    return roleMapper.listRole(po).stream()
        .filter(Objects::nonNull)
        .map(converter::toDomain)
        .collect(Collectors.toList());
  }
}
