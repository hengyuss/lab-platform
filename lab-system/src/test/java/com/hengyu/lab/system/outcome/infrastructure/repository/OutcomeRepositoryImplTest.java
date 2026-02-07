package com.hengyu.lab.system.outcome.infrastructure.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.OutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.AuthorPO;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // 1. 启动完整的 Spring 上下文
@ActiveProfiles("test") // 2. 使用 application-test.yml 配置
@Transactional // 3. 测试结束后自动回滚数据，保持环境干净
class OutcomeRepositoryTest { // IT = Integration Test

  @Autowired
  private OutcomeRepository outcomeRepository; // 被测对象

  // 注入 Mapper 为了验证数据库里的数据 (Verification)
  @Autowired
  private OutcomeMapper outcomeMapper;
  @Autowired
  private AuthorMapper authorMapper;
  @Autowired
  private PaperOutcomeMapper paperMapper;
  @Autowired
  private JdbcTemplate jdbcTemplate; // 用于直接写 SQL 准备测试数据


  @MockBean
  private ConnectionFactory connectionFactory;

  @Test
  @DisplayName("集成测试：完整保存一篇论文(主表+作者+详情)")
  void testSave_FullFlow() {
    // --- 1. 准备数据 (Arrange) ---
    PaperOutcome paper = new PaperOutcome();
    paper.setTitle("Deep Learning in Lab Systems");
    paper.setType(OutcomeType.PAPER);
    paper.setStatus(OutcomeStatus.DRAFT);

    // 准备详情
    paper.setJournalName("IEEE Transactions");
    paper.setIssn("1234-5678");

    // 准备作者
    Author author1 = Author.builder()
        .name("hengyu")
        .sort(1)
        .isCorresponding(0)
        .build();

    Author author2 = Author.builder()
        .name("ai")
        .sort(2)
        .isCorresponding(0)
        .build();
    paper.setAuthors(List.of(author1, author2));

    // --- 2. 执行操作 (Act) ---
    // 这一步会触发 Repository -> Converter -> Mapper -> H2 DB
    outcomeRepository.save(paper);

    // --- 3. 验证结果 (Assert) ---

    // A. 验证 ID 是否回填
    Long generatedId = paper.getId();
    assertThat(generatedId).isNotNull();
    System.out.println("生成的成果ID: " + generatedId);

    // B. 验证主表 (sys_outcome)
    OutcomePO outcomePO = outcomeMapper.selectById(generatedId);
    assertThat(outcomePO).isNotNull();
    assertThat(outcomePO.getTitle()).isEqualTo("Deep Learning in Lab Systems");

    // C. 验证详情表 (sys_outcome_paper)
    PaperOutcomePO paperPO = paperMapper.selectById(generatedId); // ID 应该和主表一样
    assertThat(paperPO).isNotNull();
    assertThat(paperPO.getJournalName()).isEqualTo("IEEE Transactions");
    assertThat(paperPO.getOutcomeId()).isEqualTo(generatedId);

    // D. 验证作者表 (sys_outcome_author)
    List<AuthorPO> authorPOS = authorMapper.selectList(
        new LambdaQueryWrapper<AuthorPO>().eq(AuthorPO::getOutcomeId, generatedId)
    );
    assertThat(authorPOS).hasSize(2);
    assertThat(authorPOS).extracting(AuthorPO::getAuthorName)
        .containsExactlyInAnyOrder("hengyu", "ai");
  }

  @Test
  @DisplayName("集成测试：更新流程(修改标题，替换作者)")
  void testUpdate_FullFlow() {
    // --- 1. 先存一条数据 (Setup) ---
    PaperOutcome origin = new PaperOutcome();
    origin.setTitle("Old Title");
    origin.setType(OutcomeType.PAPER);
    origin.setStatus(OutcomeStatus.DRAFT);
    origin.setIssn("testIssn");
    origin.setPublishTime(LocalDateTime.now());
    origin.setAuthors(List.of(Author.builder().name("old").sort(1).isCorresponding(0).build()));
    outcomeRepository.save(origin);

    Long id = origin.getId();

    // --- 2. 修改数据 (Modify) ---
    // 模拟从 Service 层传进来的新对象
    PaperOutcome updateCmd = new PaperOutcome();
    updateCmd.setId(id); // ID 必须存在
    updateCmd.setType(OutcomeType.PAPER);
    updateCmd.setTitle("New Title"); // 改名
    updateCmd.setIssn("updateissn");
    updateCmd.setStatus(OutcomeStatus.DRAFT);
    updateCmd.setJournalName("updateJournalName");
    updateCmd.setPublishTime(LocalDateTime.now());

    Author author1 = Author.builder()
        .name("hengyu")
        .sort(1)
        .isCorresponding(0)
        .build();

    Author author2 = Author.builder()
        .name("ai")
        .sort(2)
        .isCorresponding(0)
        .build();
    // 换一批作者
    updateCmd.setAuthors(List.of(
        author1, author2
    ));

    // --- 3. 执行更新 (Act) ---
    outcomeRepository.save(updateCmd);

    // --- 4. 验证 (Assert) ---
    // 验证主表更新
    OutcomePO newOutcomePO = outcomeMapper.selectById(id);
    assertThat(newOutcomePO.getTitle()).isEqualTo("New Title");

    PaperOutcomePO newPaperOutcomePO = paperMapper.selectById(id);
    Assertions.assertEquals("updateissn", newPaperOutcomePO.getIssn());
    Assertions.assertEquals("updateJournalName", newPaperOutcomePO.getJournalName());

    // 验证作者是否“先删后插”
    List<AuthorPO> currentAuthors = authorMapper.selectList(
        new LambdaQueryWrapper<AuthorPO>().eq(AuthorPO::getOutcomeId, id)
    );

    // 应该只有新的2个，旧的 "Old Author" 应该没了
    assertThat(currentAuthors).hasSize(2);
    assertThat(currentAuthors).extracting(AuthorPO::getAuthorName)
        .contains("hengyu", "ai")
        .doesNotContain("old");
  }


  @Test
  void delete() {
    // --- 1. 先存一条数据 (Setup) ---
    PaperOutcome origin = new PaperOutcome();
    origin.setTitle("Title");
    origin.setType(OutcomeType.PAPER);
    origin.setStatus(OutcomeStatus.DRAFT);
    origin.setIssn("testIssn");
    origin.setPublishTime(LocalDateTime.now());
    origin.setAuthors(List.of(Author.builder().name("old").sort(1).isCorresponding(0).build()));
    outcomeRepository.save(origin);

    Long id = origin.getId();

    OutcomePO newOutcomePO = outcomeMapper.selectById(id);
    assertThat(newOutcomePO.getTitle()).isEqualTo("Title");
    List<AuthorPO> currentAuthors = authorMapper.selectList(
        new LambdaQueryWrapper<AuthorPO>().eq(AuthorPO::getOutcomeId, id)
    );
    assertThat(currentAuthors).hasSize(1);
    PaperOutcomePO paperOutcomePO = paperMapper.selectById(id);
    assertThat(paperOutcomePO.getIssn()).isEqualTo("testIssn");

    outcomeRepository.delete(origin);
    OutcomePO deletedOutcome = outcomeMapper.selectById(id);
    assertThat(deletedOutcome).isNull();
    List<AuthorPO> deletedAuthors = authorMapper.selectList(
        new LambdaQueryWrapper<AuthorPO>().eq(AuthorPO::getOutcomeId, id)
    );
    assertThat(deletedAuthors).hasSize(0);
    PaperOutcomePO deletedPaperOutcomePO = paperMapper.selectById(id);
    assertThat(deletedPaperOutcomePO).isNull();
  }

  @Test
  void findById_exist() {
    // --- 1. 先存一条数据 (Setup) ---
    PaperOutcome origin = new PaperOutcome();
    Author author = Author.builder().name("old").sort(1).isCorresponding(0).build();
    origin.setTitle("Old Title");
    origin.setType(OutcomeType.PAPER);
    origin.setStatus(OutcomeStatus.DRAFT);
    origin.setIssn("testIssn");
    origin.setPublishTime(LocalDateTime.now());
    origin.setAuthors(List.of(author));
    outcomeRepository.save(origin);

    Long id = origin.getId();

    Optional<Outcome> findOutcome = outcomeRepository.findById(id);
    assertThat(findOutcome.isPresent()).isTrue();
    assertThat(findOutcome.get().getTitle()).isEqualTo("Old Title");
    assertThat(findOutcome.get().getType()).isEqualTo(OutcomeType.PAPER);
    assertThat(findOutcome.get().getStatus()).isEqualTo(OutcomeStatus.DRAFT);
    assertThat(findOutcome.get().getAuthors()).hasSize(1);
  }


  @Test
  @DisplayName("测试：策略模式+多态映射 查询论文")
  void testSelectOutcomePage_ShouldReturnPaperOutcome() {
    // === 1. Arrange (准备数据) ===
    // 我们直接用 SQL 插入数据，模拟数据库里已有的状态
    // 插入主表 (注意：type 必须是 'PAPER' 才能触发鉴别器)
    jdbcTemplate.update("INSERT INTO sys_outcome (id, title, status, type, create_time) " +
        "VALUES (1001, 'Deep Learning Research', 'PUBLISHED', 'PAPER', NOW())");

    // 插入论文扩展表
    jdbcTemplate.update(
        "INSERT INTO sys_outcome_paper (outcome_id, journal_name, issn, publish_time) " +
            "VALUES (1001, 'Nature Intelligence', 'ISSN-8888', '2023-01-01')");

    jdbcTemplate.update(
        "INSERT INTO sys_outcome_author (outcome_id, author_name, sort, is_corresponding) " +
            "VALUES (1001, 'sk', 1, 0)");
    // 构造查询参数
    OutcomePaperQry qry = new OutcomePaperQry();
    qry.setPageNo(1);
    qry.setPageSize(10);
    qry.setIssn("ISSN-8888"); // 触发具体的查询条件
    qry.setTitle("Deep"); // 触发通用查询条件

    // === 2. Act (执行查询) ===
    IPage<Outcome> resultPage = outcomeRepository.selectOutcomePage(qry);

    // === 3. Assert (验证结果) ===

    // 验证分页信息
    assertThat(resultPage.getTotal()).isEqualTo(1);
    assertThat(resultPage.getRecords()).hasSize(1);

    // 获取第一条记录
    Outcome outcome = resultPage.getRecords().get(0);

    // 🔥 核心验证：多态是否生效？
    // 验证 XML 的 <discriminator> 是否成功把行映射成了 PaperOutcome 子类
    assertThat(outcome).isInstanceOf(PaperOutcome.class);

    // 验证字段映射
    PaperOutcome paper = (PaperOutcome) outcome;
    assertThat(paper.getId()).isEqualTo(1001L);
    assertThat(paper.getTitle()).isEqualTo("Deep Learning Research");

    // 验证 JOIN 扩展表字段是否查出来了
    assertThat(paper.getJournalName()).isEqualTo("Nature Intelligence");
    assertThat(paper.getIssn()).isEqualTo("ISSN-8888");
    assertThat(paper.getAuthors()).hasSize(1);
  }


  @Test
  void testExistDblpKey(){

    jdbcTemplate.update("INSERT INTO sys_outcome (id, title, status, type, create_time) " +
        "VALUES (1001, 'Deep Learning Research', 'PUBLISHED', 'PAPER', NOW())");

    jdbcTemplate.update(
        "INSERT INTO sys_outcome_paper (outcome_id, journal_name, issn, publish_time, dblp_key) " +
            "VALUES (1001, 'Nature Intelligence', 'ISSN-8888', '2023-01-01', 'testKey')");

    jdbcTemplate.update(
        "INSERT INTO sys_outcome_author (outcome_id, author_name, sort, is_corresponding) " +
            "VALUES (1001, 'sk', 1, 0)");

    boolean result = outcomeRepository.existsByDblpKey("testKey");
    Assertions.assertTrue(result);

  }


}