package com.hengyu.lab.system.outcome.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.framework.security.TokenService;
import com.hengyu.lab.framework.utils.JwtUtils;
import com.hengyu.lab.system.outcome.application.dto.DblpTeacherDTO;
import com.hengyu.lab.system.outcome.application.service.TeacherDblpPidService;
import com.hengyu.lab.system.outcome.infrastructure.po.TeacherDblpPidPO;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TeacherDblpPidController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherDblpPidControllerTest {

  // MockMvc 是 Spring 提供的网络请求模拟神器，不需要启动真正的 Tomcat 就能发请求
  @Autowired
  private MockMvc mockMvc;

  // @MockBean 会把一个假的 Service 塞进 Spring 容器里，替换掉真实的 Service
  @MockBean
  private TeacherDblpPidService teacherDblpPidService;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private TokenService tokenService;
  @Autowired
  private ObjectMapper objectMapper;

  /**
   * 测试新增接口 (POST + 表单传参)
   */
  @Test
  void testAddTeacher_Success() throws Exception {
    String teacherName = "张三";
    String pid = "pid-123";
    DblpTeacherDTO dto = new DblpTeacherDTO();
    dto.setTeacherName(teacherName);
    dto.setPid(pid);

    // 模拟发送 POST 请求
    mockMvc.perform(post("/api/v1/dblp/teacher/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
        )
        // 期待 HTTP 状态码返回 200 OK
        .andExpect(status().isOk());

    // 验证底层的 Service 方法是否被正确提取了参数并调用了 1 次
    verify(teacherDblpPidService, times(1)).addTeacher(any(DblpTeacherDTO.class));

  }


  @Test
  @DisplayName("❌ 失败分支：拦截缺失 teacherName 的非法请求")
  void testAddTeacher_BlankName() throws Exception {
    // 1. 准备故意弄坏的数据（名字为空）
    DblpTeacherDTO dto = new DblpTeacherDTO();
    dto.setTeacherName(""); // 触发 @NotBlank
    dto.setPid("l/Ley:Michael");

    // 2. 发起请求，期待被 @Valid 拦截，抛出 400 Bad Request
    mockMvc.perform(post("/api/v1/dblp/teacher/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest()); // 如果你有全局异常处理，这里可能是 isOk()，取决于你的 R 对象封装逻辑

    // 3. 🌟 绝对防线：验证 Service 绝对没有被调用！拦截成功！
    verify(teacherDblpPidService, never()).addTeacher(any());
  }

  @Test
  @DisplayName("❌ 失败分支：拦截缺失 PID 的非法请求")
  void testAddTeacher_BlankPid() throws Exception {
    DblpTeacherDTO dto = new DblpTeacherDTO();
    dto.setTeacherName("张三");
    dto.setPid(null); // 触发 @NotBlank

    mockMvc.perform(post("/api/v1/dblp/teacher/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());

    verify(teacherDblpPidService, never()).addTeacher(any());
  }



  /**
   * 测试删除接口 (DELETE + 路径参数)
   */
  @Test
  void testRemoveTeacher_Success() throws Exception {
    String targetPid = "pid-456";

    // 模拟发送 DELETE 请求，注意这里的路径拼接方式
    mockMvc.perform(delete("/api/v1/dblp/teacher").param("pid", targetPid))
        // 期待 HTTP 状态码返回 200 OK
        .andExpect(status().isOk());

    // 验证底层的 Service 方法是否接收到了路径中的参数并执行了删除
    verify(teacherDblpPidService, times(1)).removeTeacher(targetPid);
  }


  @Test
  void testListTeachers_WithDefaultValues_Success() throws Exception {
    // Arrange: 准备一个空的假的分页结果
    IPage<TeacherDblpPidPO> mockPage = new Page<>(1, 10);
    mockPage.setRecords(Collections.emptyList());
    mockPage.setTotal(0);

    // 告诉 Service，如果有人传 1 和 10 进来，就返回这个假的分页对象
    when(teacherDblpPidService.listCurrentTeachers(1, 10)).thenReturn(mockPage);

    // Act & Assert: 模拟前端发起极其干净的 GET 请求（URL 后面不带任何问号参数）
    mockMvc.perform(get("/api/v1/dblp/teacher/list"))
        .andExpect(status().isOk());

    // Verify: 核心断言！验证 Controller 是不是偷偷把 1 和 10 塞给了 Service
    verify(teacherDblpPidService, times(1)).listCurrentTeachers(1, 10);
  }

  /**
   * 测试分页接口 - 场景 2：前端主动传入 pageNo 和 pageSize
   */
  @Test
  void testListTeachers_WithCustomParams_Success() throws Exception {
    // Arrange: 准备自定义参数
    int customPageNo = 3;
    int customPageSize = 50;

    IPage<TeacherDblpPidPO> mockPage = new Page<>(customPageNo, customPageSize);
    mockPage.setRecords(Collections.emptyList());
    mockPage.setTotal(0);

    // 告诉 Service，期待接收 3 和 50
    when(teacherDblpPidService.listCurrentTeachers(customPageNo, customPageSize)).thenReturn(
        mockPage);

    // Act & Assert: 模拟前端在 URL 后面拼接了定制化的参数
    mockMvc.perform(get("/api/v1/dblp/teacher/list")
            .param("pageNo", String.valueOf(customPageNo))
            .param("pageSize", String.valueOf(customPageSize)))
        .andExpect(status().isOk());

    // Verify: 验证 Controller 是否原封不动地把 3 和 50 透传给了 Service
    verify(teacherDblpPidService, times(1)).listCurrentTeachers(customPageNo, customPageSize);
  }

}