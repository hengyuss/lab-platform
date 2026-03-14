package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hengyu.lab.system.outcome.application.dto.PaperOutcomeDTO;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaperOutcomeQryMapper extends BaseMapper<PaperOutcomePO> {

  IPage<PaperOutcomeDTO> selectOutcomePage(IPage<PaperOutcomeDTO> page,
      @Param(Constants.WRAPPER) QueryWrapper<PaperOutcomeDTO> wrapper);
}
