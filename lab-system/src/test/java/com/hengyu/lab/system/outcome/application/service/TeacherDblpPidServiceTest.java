package com.hengyu.lab.system.outcome.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.system.outcome.application.dto.DblpTeacherDTO;
import com.hengyu.lab.system.outcome.infrastructure.mapper.TeacherDblpPidMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.TeacherDblpPidPO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeacherDblpPidServiceTest {

  // 1. 模拟底层的 Mapper，切断与真实数据库的连接
  @Mock
  private TeacherDblpPidMapper teacherDblpPidMapper;

  // 2. 将模拟出来的 Mapper 注入到我们要测试的 Service 中
  @InjectMocks
  private TeacherDblpPidService teacherDblpPidService;


  @Test
  @DisplayName("✅ 成功分支：正常添加全新的老师")
  void testAddTeacher_Success() {
    // 1. 准备参数
    DblpTeacherDTO dto = new DblpTeacherDTO();
    dto.setTeacherName("Michael Ley");
    dto.setPid("l/Ley:Michael");

    // 2. Mock 行为：告诉 mapper，当调用 exists 时（随便传什么 Wrapper），返回 false（表示数据库里没有）
    when(teacherDblpPidMapper.exists(any(LambdaQueryWrapper.class))).thenReturn(false);

    // 3. 执行测试
    assertDoesNotThrow(() -> teacherDblpPidService.addTeacher(dto));

    // 4. 验证与断言 (🌟 极度重要：验证 mapper.insert 是否真的被调用了，并且拦截传入的对象看看对不对)
    ArgumentCaptor<TeacherDblpPidPO> captor = ArgumentCaptor.forClass(TeacherDblpPidPO.class);
    verify(teacherDblpPidMapper, times(1)).insert(captor.capture());

    TeacherDblpPidPO savedPo = captor.getValue();
    assertEquals("Michael Ley", savedPo.getTeacherName());
    assertEquals("l/Ley:Michael", savedPo.getPid());
  }

  @Test
  @DisplayName("❌ 失败分支：参数为空时应抛出 IllegalArgumentException")
  void testAddTeacher_EmptyParams() {
    // 准备参数 (缺少 PID)
    DblpTeacherDTO dto = new DblpTeacherDTO();
    dto.setTeacherName("Michael Ley");
    dto.setPid("");

    // 执行并断言异常
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> teacherDblpPidService.addTeacher(dto)
    );
    assertEquals("老师姓名和pid不能为空", exception.getMessage());

    // 验证 mapper 绝对没有被调用
    verify(teacherDblpPidMapper, never()).exists(any());
    verify(teacherDblpPidMapper, never()).insert(any(TeacherDblpPidPO.class));
  }

  @Test
  @DisplayName("❌ 失败分支：PID重复时应抛出 BizException")
  void testAddTeacher_DuplicatePid() {
    // 1. 准备参数
    DblpTeacherDTO dto = new DblpTeacherDTO();
    dto.setTeacherName("张三");
    dto.setPid("12/3456");

    // 2. Mock 行为：模拟数据库中已经存在该 PID
    when(teacherDblpPidMapper.exists(any(LambdaQueryWrapper.class))).thenReturn(true);

    // 3. 执行并断言异常
    // 假设你的自定义异常叫 BizException，如果你包名不一样请自行 import
    RuntimeException exception = assertThrows(
        RuntimeException.class, // 替换为你实际的 BizException.class
        () -> teacherDblpPidService.addTeacher(dto)
    );
    assertEquals("该老师已存在，请勿重复添加", exception.getMessage());

    // 4. 验证防线：确保抛出异常后，insert 绝对没有被执行！
    verify(teacherDblpPidMapper, never()).insert(any(TeacherDblpPidPO.class));
  }


  @Test
  void testRemoveTeacher_WithEmptyPid_ShouldThrowException() {
    // 测试 null
    assertThrows(IllegalArgumentException.class, () -> {
      teacherDblpPidService.removeTeacher(null);
    });

    // 测试空字符串
    assertThrows(IllegalArgumentException.class, () -> {
      teacherDblpPidService.removeTeacher("");
    });

    // 测试纯空格
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      teacherDblpPidService.removeTeacher("   ");
    });

    // 验证异常信息是否精准对应
    assertEquals("老师pid 不能为空", exception.getMessage());

    // 终极防御校验：确保在参数错误的情况下，绝对没有去调用数据库的 delete 方法
    verify(teacherDblpPidMapper, never()).delete(any());
  }

  /**
   * 测试 removeTeacher 分支 2：传入合法 PID，正常调用 Mapper 的 delete 方法
   */
  @Test
  void testRemoveTeacher_Success() {
    // Arrange: 准备一个合法的 PID
    String validPid = "pid-12345";

    // Act: 执行删除方法
    teacherDblpPidService.removeTeacher(validPid);

    // Assert: 验证底层 Mapper 的 delete 方法被精确调用了 1 次
    // 注意：因为 LambdaQueryWrapper 是每次 new 出来的内部对象，且 MyBatis-Plus
    // 并没有重写它的 equals 方法，所以在 Mockito 里直接用 any() 匹配器是最稳妥的。
    verify(teacherDblpPidMapper, times(1)).delete(any(LambdaQueryWrapper.class));
  }


  @Test
  void testListCurrentTeachers_Success() {
    // Arrange (准备阶段)
    int pageNo = 1;
    int pageSize = 10;

    // 1. 捏造一些假数据作为数据库的返回结果
    TeacherDblpPidPO teacher1 = new TeacherDblpPidPO();
    teacher1.setTeacherName("张三");
    teacher1.setPid("pid-111");

    TeacherDblpPidPO teacher2 = new TeacherDblpPidPO();
    teacher2.setTeacherName("李四");
    teacher2.setPid("pid-222");

    List<TeacherDblpPidPO> mockRecords = Arrays.asList(teacher1, teacher2);

    // 2. 模拟底层的 Page 返回对象
    Page<TeacherDblpPidPO> mockReturnPage = new Page<>(pageNo, pageSize);
    mockReturnPage.setRecords(mockRecords);
    mockReturnPage.setTotal(2); // 假设总共有 2 条数据

    // 3. 给 Mapper 打桩：当调用 selectPage 时，强制返回我们捏造的 mockReturnPage
    // 注意细节：因为你代码里 wrapper 传的是 null，所以这里必须用 isNull() 匹配器
    when(teacherDblpPidMapper.selectPage(any(Page.class), isNull())).thenReturn(mockReturnPage);

    // Act (执行阶段)
    IPage<TeacherDblpPidPO> result = teacherDblpPidService.listCurrentTeachers(pageNo, pageSize);

    // Assert (断言阶段)
    // 1. 验证返回值是否和我们预期的一模一样
    assertNotNull(result);
    assertEquals(2, result.getTotal()); // 验证总条数
    assertEquals(2, result.getRecords().size()); // 验证当前页的数据量
    assertEquals("张三", result.getRecords().get(0).getTeacherName()); // 验证具体数据

    // 2. 最关键的一步：拦截 Service 传给 Mapper 的 Page 对象，检查参数有没有传错
    ArgumentCaptor<Page<TeacherDblpPidPO>> pageCaptor = ArgumentCaptor.forClass(Page.class);

    // 验证 selectPage 被精确调用了 1 次，并抓取它吃进去的第一个参数
    verify(teacherDblpPidMapper, times(1)).selectPage(pageCaptor.capture(), isNull());

    // 拆开抓取到的 Page 对象，验证它的 current 和 size 是否等于我们最初传入的 1 和 10
    Page<TeacherDblpPidPO> capturedPage = pageCaptor.getValue();
    assertEquals((long) pageNo, capturedPage.getCurrent());
    assertEquals((long) pageSize, capturedPage.getSize());
  }


  @Test
  void testListCurrentTeachers_All_Success() {
    // Arrange (准备阶段)：手工捏造两行假数据，模拟数据库里的所有老师名单
    TeacherDblpPidPO teacher1 = new TeacherDblpPidPO();
    teacher1.setId(1); // 结合刚才的修复，这里用 Integer
    teacher1.setTeacherName("张三");
    teacher1.setPid("pid-111");

    TeacherDblpPidPO teacher2 = new TeacherDblpPidPO();
    teacher2.setId(2);
    teacher2.setTeacherName("李四");
    teacher2.setPid("pid-222");

    List<TeacherDblpPidPO> mockDbList = Arrays.asList(teacher1, teacher2);

    // 给 Mapper 打桩：当执行 selectList 且参数为 null 时，把我们捏造的假数据抛出去
    when(teacherDblpPidMapper.selectList(isNull())).thenReturn(mockDbList);

    // Act (执行阶段)：调用 Service 层的方法
    List<TeacherDblpPidPO> result = teacherDblpPidService.listCurrentTeachers();

    // Assert (断言阶段)
    // 1. 验证返回的结果集是不是非空的，并且长度等于 2
    assertNotNull(result);
    assertEquals(2, result.size());

    // 2. 抽查一下里面的数据，看看有没有在 Service 层被意外篡改
    assertEquals("张三", result.get(0).getTeacherName());
    assertEquals("pid-222", result.get(1).getPid());

    // 3. 终极验证：确认底层 Mapper 的 selectList 方法被精准调用了 1 次，并且吃进去的参数确实是 null
    verify(teacherDblpPidMapper, times(1)).selectList(isNull());
  }

}