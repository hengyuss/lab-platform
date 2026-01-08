package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthorMapper extends BaseMapper<AuthorPO> {


    @Delete("DELETE FROM sys_outcome_author WHERE outcome_id = #{outcomeId}")
    int deleteByOutcomeId(@Param("outcomeId") Long outcomeId);

    void insertBatch(@Param("authorPOs") List<AuthorPO> authorPOs);

    @Select("select * from sys_outcome_author WHERE outcome_id = #{outcomeId}")
    @ResultMap("AuthorResultMap")
    List<Author> selectByOutcomeId(@Param("outcomeId") Long outcomeId);
}
