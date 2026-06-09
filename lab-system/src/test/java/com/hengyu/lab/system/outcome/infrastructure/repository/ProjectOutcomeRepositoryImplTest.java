package com.hengyu.lab.system.outcome.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.hengyu.lab.system.outcome.domain.ProjectOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.constant.ProjectType;
import com.hengyu.lab.system.outcome.domain.repository.PaperOutcomeRepository;
import com.hengyu.lab.system.outcome.domain.repository.ProjectOutcomeRepository;
import com.hengyu.lab.system.outcome.domain.valobj.ProjectIndicator;
import com.hengyu.lab.system.outcome.domain.valobj.ResponsiblePerson;
import com.hengyu.lab.system.outcome.infrastructure.mapper.AuthorMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.OutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.mapper.ProjectOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import com.hengyu.lab.system.outcome.infrastructure.po.ProjectOutcomePO;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.WebContentGenerator;


@SpringBootTest // 1. 启动完整的 Spring 上下文
@ActiveProfiles("test") // 2. 使用 application-test.yml 配置
@Transactional // 3. 测试结束后自动回滚数据，保持环境干净
class ProjectOutcomeRepositoryImplTest {
  @Autowired
  private ProjectOutcomeRepository projectOutcomeRepository;

  @Autowired
  private OutcomeMapper outcomeMapper;

  @Autowired
  private ProjectOutcomeMapper projectOutcomeMapper;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @MockBean
  private ConnectionFactory connectionFactory;
  @Autowired
  private WebContentGenerator webContentGenerator;


  private ProjectOutcome buildMockProjectOutcome() {
    ProjectOutcome project = new ProjectOutcome();
    // 1. 父类属性 (请根据实际 Outcome 类的属性修改)
    // project.setName("国家自然科学基金重点项目");
    project.setTitle("test");
    project.setType(OutcomeType.PROJECT);
    project.setStatus(OutcomeStatus.PROCESSING);

    // 2. 集合属性 (测试 JSON 序列化)
    project.setFund(Arrays.asList("国家自然科学基金", "省重点研发计划"));

    // 假设 ResponsiblePerson 包含 name 和 title 两个属性
    // 注意：ResponsiblePerson 类必须有无参构造，且建议重写 equals 方法
    ResponsiblePerson p1 = new ResponsiblePerson();
    // p1.setName("张三");
    ResponsiblePerson p2 = new ResponsiblePerson();
    // p2.setName("李四");
    project.setResponsiblePersons(Arrays.asList(p1, p2));

    // 3. 枚举属性 (测试枚举映射)
    // 请替换为你的实际枚举值
    project.setProjectType(ProjectType.GUANGXI_NSF_KEY_PROJECT);
    project.setProjectIndicator(new ProjectIndicator(1L, "test"));

    // 4. 时间属性
    project.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0));
    project.setEndTime(LocalDateTime.of(2026, 12, 31, 23, 59));

    return project;
  }

  @Test
  public void testSave_WithFullData_ShouldInsertCorrectly() {
    // 1. 准备数据
    ProjectOutcome newProject = buildMockProjectOutcome();

    // 2. 执行保存
    projectOutcomeRepository.save(newProject);
    Long generatedId = newProject.getId(); // 父类继承下来的 ID

    // 3. 验证基础表插入成功
    assertNotNull(generatedId, "领域对象应该被回写了 ID");
    OutcomePO basePo = outcomeMapper.selectById(generatedId);
    assertNotNull(basePo, "基础表应该存在数据");

    // 4. 重点验证：扩展表的全字段映射是否正确
    ProjectOutcomePO savedPo = projectOutcomeMapper.selectById(generatedId);
    System.out.println("------------------" +savedPo);
    assertNotNull(savedPo, "扩展表应该存在数据");

    // 验证时间
    assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), savedPo.getStartTime());

    // 验证枚举 (如果映射失败，这里取出来的会是 null)
    assertEquals(ProjectType.GUANGXI_NSF_KEY_PROJECT, savedPo.getProjectType());

    // 验证 JSON 集合 (测试 JacksonTypeHandler)
    assertNotNull(savedPo.getFund());
    assertEquals(2, savedPo.getFund().size());
    assertEquals("国家自然科学基金", savedPo.getFund().get(0));

    assertNotNull(savedPo.getResponsiblePersons());
    assertEquals(2, savedPo.getResponsiblePersons().size());
  }

  @Test
  public void testSave_UpdateExistingData_ShouldUpdateCorrectly() {
    // 1. 先插入一条基础数据
    ProjectOutcome project = buildMockProjectOutcome();
    projectOutcomeRepository.save(project);
    Long id = project.getId();

    // 2. 模拟修改业务逻辑：增加一个基金，延长结束时间
    List<String> newFunds = Arrays.asList("国家自然科学基金", "省重点研发计划", "市级创新基金");
    project.setFund(newFunds);
    project.setEndTime(LocalDateTime.of(2027, 6, 30, 23, 59));
    // project.setProjectIndicator(ProjectIndicator.TYPE_B); // 也可以测试修改枚举

    // 3. 执行更新
    projectOutcomeRepository.save(project);

    // 4. 重新从数据库查询，验证是否更新成功
    ProjectOutcomePO updatedPo = projectOutcomeMapper.selectById(id);

    // 验证时间是否被更新
    assertEquals(LocalDateTime.of(2027, 6, 30, 23, 59), updatedPo.getEndTime());
    // 验证 JSON 数组是否被更新
    assertEquals(3, updatedPo.getFund().size());
    assertTrue(updatedPo.getFund().contains("市级创新基金"));
  }



}