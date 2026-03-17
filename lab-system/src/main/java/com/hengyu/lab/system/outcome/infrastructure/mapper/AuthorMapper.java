package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hengyu.lab.system.outcome.application.dto.AuthorDTO;
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

  @Select("select * from sys_outcome_author WHERE outcome_id = #{outcomeId}")
  List<AuthorPO> selectAllByOutcomeId(@Param("outcomeId") Long outcomeId);


  @Select({
      "<script>",
      "SELECT *, author_name AS name",
      "FROM sys_outcome_author ",
      "WHERE outcome_id IN ",
      "<foreach item='id' collection='outcomeIds' open='(' separator=',' close=')'>",
      "#{id}",
      "</foreach>",
      "ORDER BY id ASC",
      "</script>"
  })
  List<AuthorDTO> selectAuthorsByOutcomeIds(@Param("outcomeIds") List<Long> outcomeIds);

  @Select("<script>" +
      "select outcome_id from sys_outcome_author " +
      "<where> " +
      "  <if test='name != null and name != \"\"'> " +
      "    and author_name like concat(#{name}, '%') " +
      "  </if> " +
      "  <if test='sort != null'> " +
      "    and sort = #{sort} " +
      "  </if> " +
      "  <if test='isCorresponding != null'> " +
      "    and is_corresponding = #{isCorresponding} " +
      "  </if> " +
      "</where>" +
      "</script>")
  List<Long> selectOutcomeIdByAuthorNameAndSort(@Param("name") String name,
      @Param("sort") Integer sort, @Param("isCorresponding")  Integer isCorresponding);
}
