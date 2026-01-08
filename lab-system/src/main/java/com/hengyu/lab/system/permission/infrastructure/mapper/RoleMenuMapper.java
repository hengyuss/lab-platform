package com.hengyu.lab.system.permission.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.permission.infrastructure.po.RoleMenuPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuPO> {

  int insertRoleMenuBatch(@Param("list") List<RoleMenuPO> list);

}
