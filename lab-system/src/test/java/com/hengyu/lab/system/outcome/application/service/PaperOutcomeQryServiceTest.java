package com.hengyu.lab.system.outcome.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.outcome.application.builder.PaperOutcomeConditionBuilder;
import com.hengyu.lab.system.outcome.application.dto.AuthorDTO;
import com.hengyu.lab.system.outcome.application.dto.PaperOutcomeDTO;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeQryMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaperOutcomeQryServiceTest {

  @Mock
  private PaperOutcomeQryMapper paperOutcomeQryMapper;

  @Mock
  private AuthorMapper authorMapper;

  // ✨ 注入你刚刚重命名的高级 Builder
  @Mock
  private PaperOutcomeConditionBuilder<PaperOutcomeDTO> paperOutcomeConditionBuilder;

  @InjectMocks
  private PaperOutcomeQryService paperOutcomeQryService;

  @Test
  void selectOutcomePage_ShouldReturnFast_WhenNoRecordsFound() {
    // Arrange (准备)
    OutcomePaperQry qry = new OutcomePaperQry();
    qry.setPageNo(1);
    qry.setPageSize(10);

    Page<PaperOutcomeDTO> emptyPage = new Page<>();
    emptyPage.setRecords(Collections.emptyList());

    // ✨ 魔法 Mock：拦截 buildSearchCondition，原样返回传入的 wrapper
    when(paperOutcomeConditionBuilder.buildSearchCondition(any(QueryWrapper.class), eq(qry)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // 模拟数据库查不到数据
    when(paperOutcomeQryMapper.selectOutcomePage(any(Page.class), any())).thenReturn(emptyPage);

    // Act (执行)
    IPage<PaperOutcomeDTO> result = paperOutcomeQryService.selectOutcomePage(qry);

    // Assert (断言)
    assertTrue(result.getRecords().isEmpty());
    // 核心验证：防空拦截生效，绝不会去查作者表！
    verify(authorMapper, never()).selectAuthorsByOutcomeIds(anyList());
    verify(paperOutcomeConditionBuilder, times(1)).buildSearchCondition(any(), eq(qry));
  }

  @Test
  void selectOutcomePage_ShouldStitchAuthorsCorrectly_WhenDataExists() {
    // Arrange (准备)
    OutcomePaperQry qry = new OutcomePaperQry();
    qry.setPageNo(1);
    qry.setPageSize(10);

    PaperOutcomeDTO paper1 = new PaperOutcomeDTO(); paper1.setId(101L);
    PaperOutcomeDTO paper2 = new PaperOutcomeDTO(); paper2.setId(102L);
    Page<PaperOutcomeDTO> mockPage = new Page<>();
    mockPage.setRecords(List.of(paper1, paper2));

    AuthorDTO author1 = new AuthorDTO(); author1.setId(1); author1.setOutcomeId(101L); author1.setName("张三");
    AuthorDTO author2 = new AuthorDTO(); author2.setId(2); author2.setOutcomeId(101L); author2.setName("李四");
    AuthorDTO author3 = new AuthorDTO(); author3.setId(3); author3.setOutcomeId(102L); author3.setName("王五");

    // Mock 行为
    when(paperOutcomeConditionBuilder.buildSearchCondition(any(QueryWrapper.class), eq(qry)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(paperOutcomeQryMapper.selectOutcomePage(any(Page.class), any())).thenReturn(mockPage);
    when(authorMapper.selectAuthorsByOutcomeIds(List.of(101L, 102L))).thenReturn(List.of(author1, author2, author3));

    // Act (执行)
    IPage<PaperOutcomeDTO> result = paperOutcomeQryService.selectOutcomePage(qry);

    // Assert (断言)
    List<PaperOutcomeDTO> records = result.getRecords();
    assertEquals(2, records.size(), "应该返回两篇论文");

    assertEquals(2, records.get(0).getAuthors().size(), "论文101应该有2个作者");
    assertEquals("张三", records.get(0).getAuthors().get(0).getName());

    assertEquals(1, records.get(1).getAuthors().size(), "论文102应该有1个作者");
    assertEquals("王五", records.get(1).getAuthors().get(0).getName());
  }

  @Test
  void selectOutcomePage_ShouldSetEmptyList_WhenPaperHasNoAuthors() {
    // Arrange (准备)
    OutcomePaperQry qry = new OutcomePaperQry();
    qry.setPageNo(1);
    qry.setPageSize(10);

    PaperOutcomeDTO paper1 = new PaperOutcomeDTO(); paper1.setId(101L);
    Page<PaperOutcomeDTO> mockPage = new Page<>();
    mockPage.setRecords(List.of(paper1));

    // Mock 行为：模拟这篇论文在数据库里还没有录入任何作者
    when(paperOutcomeConditionBuilder.buildSearchCondition(any(QueryWrapper.class), eq(qry)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(paperOutcomeQryMapper.selectOutcomePage(any(Page.class), any())).thenReturn(mockPage);
    when(authorMapper.selectAuthorsByOutcomeIds(List.of(101L))).thenReturn(Collections.emptyList());

    // Act (执行)
    IPage<PaperOutcomeDTO> result = paperOutcomeQryService.selectOutcomePage(qry);

    // Assert (断言)
    List<PaperOutcomeDTO> records = result.getRecords();
    assertNotNull(records.get(0).getAuthors(), "作者集合绝不能为null，必须被getOrDefault兜底");
    assertTrue(records.get(0).getAuthors().isEmpty(), "作者集合应该为空列表");
  }
}