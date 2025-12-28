package com.hengyu.lab.system.permission.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.permission.infrastructure.po.MenuPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends BaseMapper<MenuPO> {

  List<MenuPO> selectMenuList(MenuPO po);

}
