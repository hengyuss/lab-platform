package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaperOutcomeStrategyTest {


  @Mock
  PaperOutcomeMapper paperOutcomeMapper;

  @Mock
  PaperOutcomeConverter paperConverter;

  @InjectMocks
  PaperOutcomeStrategy outcomePaperStrategy;


  private PaperOutcome paperOutcome;
  private Author author1;
  private Author author2;
  private Author author3;

  @BeforeEach
  void setUp() {
    paperOutcome = new PaperOutcome();

    // 假设 Author 有个全参构造：Author(id, name, isCorresponding)
    // 初始状态下，大家都是普通作者 (NORMAL)
    author1 = Author.builder().id(1).name("张三").isCorresponding(Author.NOT_CORRESPONDING).build();
    author2 = Author.builder().id(2).name("李四").isCorresponding(Author.NOT_CORRESPONDING).build();
    author3 = Author.builder().id(3).name("王五").isCorresponding(Author.NOT_CORRESPONDING).build();

    List<Author> authors = new ArrayList<>(Arrays.asList(author1, author2, author3));
    paperOutcome.setAuthors(authors);
  }

  @Test
  void test_getType() {
    OutcomeType outcomeType = outcomePaperStrategy.getOutcomeType();
    Assertions.assertEquals(OutcomeType.PAPER, outcomeType);
  }

  @Test
  void test_set_details() {
    PaperOutcome paperOutcome = PaperOutcome.builder().build();
    Mockito.when(paperConverter.toPO(paperOutcome)).thenReturn(PaperOutcomePO.builder().build());
    outcomePaperStrategy.saveDetails(paperOutcome);
    Mockito.verify(paperOutcomeMapper).insert(any(PaperOutcomePO.class));
  }

  @Test
  void delete_details() {
    PaperOutcome paperOutcome = PaperOutcome.builder().build();
    Mockito.when(paperConverter.toPO(paperOutcome)).thenReturn(PaperOutcomePO.builder().build());
    outcomePaperStrategy.deleteDetails(paperOutcome);
    Mockito.verify(paperOutcomeMapper).deleteById(any(PaperOutcomePO.class));
  }

  @Test
  @DisplayName("正常情况：成功设置指定的多个通讯作者")
  void shouldAssignCorrespondingAuthorsSuccessfully() {
    // 准备参数：我们要把 张三(1) 和 王五(3) 设为通讯作者
    List<Integer> correspondingIds = Arrays.asList(1, 3);

    // 执行领域方法
    paperOutcome.assignCorresponding(correspondingIds);

    // 断言验证 (AssertJ 风格，极其好读)
    assertThat(author1.getIsCorresponding()).isEqualTo(Author.CORRESPONDING);
    assertThat(author2.getIsCorresponding()).isEqualTo(Author.NOT_CORRESPONDING); // 李四没变
    assertThat(author3.getIsCorresponding()).isEqualTo(Author.CORRESPONDING);
  }

  @Test
  @DisplayName("边界情况：传入的 ID 列表中包含本论文不存在的作者 ID")
  void shouldIgnoreNonExistentAuthorIds() {
    // 准备参数：99 是一个不存在的作者 ID，1 是张三
    List<Integer> correspondingIds = Arrays.asList(1, 99);

    // 执行领域方法（不应该报错，且只处理存在的作者）
    paperOutcome.assignCorresponding(correspondingIds);

    // 断言验证
    assertThat(author1.getIsCorresponding()).isEqualTo(Author.CORRESPONDING);
    assertThat(author2.getIsCorresponding()).isEqualTo(Author.NOT_CORRESPONDING);
    assertThat(author3.getIsCorresponding()).isEqualTo(Author.NOT_CORRESPONDING);
  }

  @Test
  @DisplayName("边界情况：传入空列表，不应报错且无影响")
  void shouldHandleEmptyList() {
    // 准备参数：空列表
    List<Integer> emptyIds = Collections.emptyList();

    // 执行领域方法
    paperOutcome.assignCorresponding(emptyIds);

    // 断言验证：所有人都保持原样
    assertThat(author1.getIsCorresponding()).isEqualTo(Author.NOT_CORRESPONDING);
    assertThat(author2.getIsCorresponding()).isEqualTo(Author.NOT_CORRESPONDING);
    assertThat(author3.getIsCorresponding()).isEqualTo(Author.NOT_CORRESPONDING);
  }

}