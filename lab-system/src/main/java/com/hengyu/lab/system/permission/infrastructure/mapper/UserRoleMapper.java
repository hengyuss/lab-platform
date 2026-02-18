package com.hengyu.lab.system.permission.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.permission.infrastructure.po.UserRolePO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRolePO> {

  int insertUserRoleBatch(@Param("list") List<UserRolePO> list);

  @Select("select role_id from sys_user_role where user_id = #{userId}")
  List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

}
