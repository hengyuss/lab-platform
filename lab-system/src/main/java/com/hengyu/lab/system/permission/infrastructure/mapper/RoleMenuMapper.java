package com.hengyu.lab.system.permission.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.permission.infrastructure.po.RoleMenuPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuPO> {

  int insertRoleMenuBatch(@Param("list") List<RoleMenuPO> list);

}
