package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaperOutcomeMapper extends BaseMapper<PaperOutcomePO> {

  @Select("select 1 from sys_outcome_paper where dblp_key = #{dblpKey} limit 1")
  Boolean existsByDblpKey(@Param("dblpKey") String dblpKey);

}
