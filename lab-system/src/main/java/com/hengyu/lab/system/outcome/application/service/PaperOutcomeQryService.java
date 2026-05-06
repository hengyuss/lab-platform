package com.hengyu.lab.system.outcome.application.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.outcome.application.builder.PaperOutcomeConditionBuilder;
import com.hengyu.lab.system.outcome.application.dto.AuthorDTO;
import com.hengyu.lab.system.outcome.application.dto.PaperOutcomeDTO;
import com.hengyu.lab.system.outcome.application.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeQryMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaperOutcomeQryService {

  private final PaperOutcomeQryMapper paperOutcomeQryMapper;
  private final AuthorMapper authorMapper;
  private final PaperOutcomeConditionBuilder<PaperOutcomeDTO> paperOutcomeConditionBuilder;

  public IPage<PaperOutcomeDTO> selectOutcomePage(OutcomePaperQry qry) {
    Page<PaperOutcomeDTO> page = new Page<>(qry.getPageNo(), qry.getPageSize());
    QueryWrapper<PaperOutcomeDTO> wrapper = new QueryWrapper<>();

    QueryWrapper<PaperOutcomeDTO> finalWrapper = paperOutcomeConditionBuilder.buildSearchCondition(
        wrapper, qry);

    IPage<PaperOutcomeDTO> paperOutcomeDTOIPage = paperOutcomeQryMapper.selectOutcomePage(page,
        finalWrapper);
    List<PaperOutcomeDTO> records = paperOutcomeDTOIPage.getRecords();

    if (CollectionUtils.isEmpty(records)) {
      return paperOutcomeDTOIPage;
    }

    List<AuthorDTO> authorDTOS = authorMapper.selectAuthorsByOutcomeIds(
        records.stream().map(PaperOutcomeDTO::getId).toList());
    Map<Long, List<AuthorDTO>> authorsMap = authorDTOS.stream()
        .collect(Collectors.groupingBy(AuthorDTO::getOutcomeId));
    records.forEach(paperOutcomeDTO -> {
      paperOutcomeDTO.setAuthors(
          authorsMap.getOrDefault(paperOutcomeDTO.getId(), new ArrayList<>()));
    });
    return paperOutcomeDTOIPage;
  }

}
