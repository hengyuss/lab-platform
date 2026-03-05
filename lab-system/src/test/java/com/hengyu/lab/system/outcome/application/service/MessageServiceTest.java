package com.hengyu.lab.system.outcome.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.system.outcome.application.dto.command.PaperMessage;
import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.vo.PaperMetaTask;
import com.hengyu.lab.system.outcome.infrastructure.po.TeacherDblpPidPO;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock
  private TeacherDblpPidService teacherDblpPidService;

  @Mock
  private PaperMQGateway paperMQGateway;

  @InjectMocks
  private MessageService paperMessageService; // 替换为你实际的 Service 类名

  /**
   * 分支 1：单人爬取任务 期望：只发一条 MQ，且绝对不调用数据库全量查询
   */
  @Test
  void testSendPaperMessage_SingleTeacher_ShouldPublishOnceAndReturn() {
    // Arrange
    PaperMessage message = new PaperMessage();
    message.setTeacherName("张三");
    message.setTeacherPid("pid-123");

    // Act
    paperMessageService.sendPaperMessage(message);

    // Assert
    // 1. 验证 MQ 发送接口被精确调用了 1 次
    ArgumentCaptor<PaperMetaTask> taskCaptor = ArgumentCaptor.forClass(PaperMetaTask.class);
    verify(paperMQGateway, times(1)).publishTask(taskCaptor.capture());

    // 2. 验证发送的数据是否被正确拷贝
    PaperMetaTask capturedTask = taskCaptor.getValue();
    assertEquals("张三", capturedTask.getTeacherName());

    // 3. 终极防御校验：确认卫语句的 return 生效了，绝对没有去查数据库！
    verify(teacherDblpPidService, never()).listCurrentTeachers();
  }

  /**
   * 分支 2：全量爬取任务，但是数据库里一个老师都没有配置 期望：查了数据库，但没有发任何 MQ 消息
   */
  @Test
  void testSendPaperMessage_AllTeachers_EmptyDB_ShouldNotPublish() {
    // Arrange
    PaperMessage message = new PaperMessage();
    message.setTeacherName(PaperMetaTask.DEFAULT_TEACHER_NAME);

    // 模拟数据库返回空列表
    when(teacherDblpPidService.listCurrentTeachers()).thenReturn(Collections.emptyList());

    // Act
    paperMessageService.sendPaperMessage(message);

    // Assert
    verify(teacherDblpPidService, times(1)).listCurrentTeachers();
    // 验证空拦截器生效，绝对没有给 MQ 发送垃圾消息
    verify(paperMQGateway, never()).publishTask(any());
  }

  /**
   * 分支 3：全量爬取任务，数据库里有 2 个老师 期望：查数据库，并发了 2 条 MQ 消息
   */
  @Test
  void testSendPaperMessage_AllTeachers_WithData_ShouldPublishMultipleTimes() {
    // Arrange
    PaperMessage message = new PaperMessage();
    message.setTeacherName(PaperMetaTask.DEFAULT_TEACHER_NAME);

    // 捏造数据库返回的名单
    TeacherDblpPidPO t1 = new TeacherDblpPidPO();
    t1.setTeacherName("老师A");
    t1.setPid("pid-A");
    TeacherDblpPidPO t2 = new TeacherDblpPidPO();
    t2.setTeacherName("老师B");
    t2.setPid("pid-B");
    when(teacherDblpPidService.listCurrentTeachers()).thenReturn(Arrays.asList(t1, t2));

    // Act
    paperMessageService.sendPaperMessage(message);

    // Assert
    // 1. 验证查了 1 次数据库
    verify(teacherDblpPidService, times(1)).listCurrentTeachers();

    // 2. 验证给 MQ 发了 2 次消息！
    ArgumentCaptor<PaperMetaTask> taskCaptor = ArgumentCaptor.forClass(PaperMetaTask.class);
    verify(paperMQGateway, times(2)).publishTask(taskCaptor.capture());

    // 3. 拆开捕获到的 2 条消息，分别验证内容
    List<PaperMetaTask> capturedTasks = taskCaptor.getAllValues();
    assertEquals("老师A", capturedTasks.get(0).getTeacherName());
    assertEquals("pid-A", capturedTasks.get(0).getTeacherPid());
    assertEquals("老师B", capturedTasks.get(1).getTeacherName());
    assertEquals("pid-B", capturedTasks.get(1).getTeacherPid());
  }
}