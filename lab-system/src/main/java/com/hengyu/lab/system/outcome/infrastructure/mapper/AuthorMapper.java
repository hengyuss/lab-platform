package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorMapper extends BaseMapper<AuthorPO> {


    @Delete("DELETE FROM sys_outcome_author WHERE outcome_id = #{outcomeId}")
    int deleteByOutcomeId(@Param("outcomeId") Long outcomeId);

    void insertBatch(@Param("authorPOs") List<AuthorPO> authorPOs);
}
