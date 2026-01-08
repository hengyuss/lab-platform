package com.hengyu.lab.system.user.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.user.infrastructure.po.UserRolePO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

  int insertUserRoleBatch(@Param("list") List<UserRolePO> list);

}
