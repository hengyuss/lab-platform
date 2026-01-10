package com.hengyu.lab.system.outcome.infrastructure.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OutcomeMapper extends BaseMapper<OutcomePO> {
  Outcome findById(Long id);

  IPage<Outcome> selectPageDomain(Page<Outcome> page, @Param(Constants.WRAPPER)QueryWrapper<Outcome> wrapper);
}
