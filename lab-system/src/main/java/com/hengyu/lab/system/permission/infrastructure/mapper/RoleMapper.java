package com.hengyu.lab.system.permission.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.permission.infrastructure.po.RolePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<RolePO> {
  List<RolePO>  listRole(RolePO po);
}
