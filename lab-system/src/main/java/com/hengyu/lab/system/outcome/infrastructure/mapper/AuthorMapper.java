package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AuthorMapper extends BaseMapper<AuthorPO> {


  @Update("UPDATE sys_outcome_author SET deleted = 1 WHERE outcome_id = #{outcomeId} AND deleted = 0")
  int deleteByOutcomeId(@Param("outcomeId") Long outcomeId);

  void insertBatch(@Param("authorPOs") List<AuthorPO> authorPOs);

  @Select("select * from sys_outcome_author WHERE outcome_id = #{outcomeId}")
  @ResultMap("AuthorResultMap")
  List<Author> selectByOutcomeId(@Param("outcomeId") Long outcomeId);

  @Select("<script>" +
      "select outcome_id from sys_outcome_author " +
      "<where> " +
      "  <if test='name != null and name != \"\"'> " +
      "    and author_name like concat(#{name}, '%') " +
      "  </if> " +
      "  <if test='sort != null'> " +
      "    and sort = #{sort} " +
      "  </if> " +
      "</where>" +
      "</script>")
  List<Long> selectOutcomeIdByAuthorNameAndSort(@Param("name") String name,
      @Param("sort") Integer sort);
}
