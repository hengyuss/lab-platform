package com.hengyu.lab.system.outcome.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.framework.security.TokenService;
import com.hengyu.lab.framework.utils.JwtUtils;
import com.hengyu.lab.system.outcome.application.dto.command.PaperMessage;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.application.service.MessageService;
import com.hengyu.lab.system.outcome.application.service.OutcomeService;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = OutcomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class OutcomeControllerTest {

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private MessageService messageService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OutcomeService outcomeService;

  @Test
  void save_outcome_success() throws Exception {
    SavePaperOutcomeCmd cmd = SavePaperOutcomeCmd.builder().title("testTitle").issn("testIssn")
        .type(OutcomeType.PAPER).status(OutcomeStatus.DRAFT).journalName("testJournal")
        .publishTime(LocalDateTime.now()).build();

    Mockito.when(outcomeService.saveOutcome(cmd)).thenReturn(123L);
    mockMvc.perform(post("/api/v1/outcomes/paper").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cmd))).andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value("123"));
  }

  @Test
  void delete_outcome() throws Exception {
    mockMvc.perform(delete("/api/v1/outcomes/paper/{id}", "1")).andDo(print())
        .andExpect(status().isOk());

    verify(outcomeService).deleteOutcome(1L);
  }

  @Test
  void select_outcome_page() throws Exception {
    OutcomePaperQry outcomePaperQry = new OutcomePaperQry();
    PaperOutcome origin = new PaperOutcome();
    origin.setTitle("Title");
    origin.setType(OutcomeType.PAPER);
    origin.setStatus(OutcomeStatus.DRAFT);
    origin.setIssn("testIssn");
    origin.setPublishTime(LocalDateTime.now());
    origin.setAuthors(List.of(Author.builder().name("old").sort(1).isCorresponding(0).build()));

    outcomePaperQry.setPageNo(1);
    outcomePaperQry.setPageSize(10);
    IPage<Outcome> page = new Page<>();
    page.setTotal(1);
    page.setRecords(List.of(origin));
    Mockito.when(outcomeService.selectOutcomePage(outcomePaperQry)).thenReturn(page);

    mockMvc.perform(get("/api/v1/outcomes").param("pageNo", "1").param("pageSize", "10"))
        .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.data.total").value(1))
        .andExpect(jsonPath("$.data.records[0].title").value("Title"))
        .andExpect(jsonPath("$.data.records[0].issn").value("testIssn"));

  }


  @Test
  @DisplayName("发送消息成功：当参数合法时，应返回 200 并调用 Service")
  void sendPaperMessage_ShouldReturnOk_WhenInputIsValid() throws Exception {
    // --- 1. 准备数据 (Arrange) ---
    // 假设 PaperMessage 是 DTO 或 Value Object
    PaperMessage requestDto = new PaperMessage();
    requestDto.setTeacherName("Yong Ding");

    // --- 2. 执行请求 (Act) ---
    mockMvc.perform(post("/api/v1/outcomes/paper/message").contentType(
                MediaType.APPLICATION_JSON) // 设置 Header: Content-Type
            .content(objectMapper.writeValueAsString(requestDto))) // 把对象转成 JSON String

        // --- 3. 验证 HTTP 响应 (Assert) ---
        .andExpect(status().isOk()) // 期望 HTTP 200
    // 假设你的 R 类有一个 code 字段，值为 200 或 0
    // .andExpect(jsonPath("$.code").value(200))
    // 假设你的 R 类有一个 msg 字段
    // .andExpect(jsonPath("$.msg").value("success"));
    ;

    // --- 4. 验证业务逻辑是否被调用 (Verify) ---
    // 关键：验证 messageService.sendPaperMessage 方法确实被调用了一次
    // 并且参数的内容和我们传进去的一样
    verify(messageService).sendPaperMessage(refEq(requestDto));
  }

  @Test
  @DisplayName("当 teacherName 为空时，应使用默认值 'all' 并返回 200")
  void sendPaperMessage_ShouldUseDefault_WhenTeacherNameIsNull() throws Exception {
    // --- 1. 准备数据 ---
    // 这里的 dto 里面 teacherName 应该是 null 或者你在 DTO 里写的初始值
    PaperMessage emptyDto = new PaperMessage();
    // 确保你的 DTO 序列化出去时不包含 teacherName，或者包含 null
    // 如果你在 DTO 里直接写 private String teacherName = "all";
    // 那么 objectMapper.writeValueAsString(emptyDto) 生成的 JSON 就已经是 {"teacherName": "all"} 了
    // 这其实是在测试前端传了默认值，而不是后端处理默认值。

    // 如果你想测试“前端啥也没传，后端默认为 all”，建议直接构造 JSON 字符串：

    // --- 2. 执行请求 ---
    mockMvc.perform(post("/api/v1/outcomes/paper/message").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(emptyDto))) // 把对象转成 JSON String

        // --- 3. 验证 HTTP 状态 ---
        .andExpect(status().isOk()); // 因为有了默认值，所以应该是成功的

    // --- 4. 关键步骤：捕获 Service 接收到的参数 ---
    // 4.1 创建一个“捕获笼子”
    ArgumentCaptor<PaperMessage> captor = ArgumentCaptor.forClass(PaperMessage.class);

    // 4.2 验证 service 方法被调用，并把参数“抓”进笼子里
    verify(messageService).sendPaperMessage(captor.capture());

    // 4.3 从笼子里拿出对象进行断言
    PaperMessage capturedMessage = captor.getValue();

    // 验证：虽然前端没传，但在 Service 层接收到时，它应该是 "all"
    assertEquals("all", capturedMessage.getTeacherName());
  }

  @Test
  void uploadFile_success() throws Exception {
    Long outcomeId = 1L;
    String fileName = "testFileName";
    String path = "testPath";

    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName,
        MediaType.APPLICATION_PDF_VALUE, "fake content".getBytes());

    when(outcomeService.uploadPaperFile(eq(outcomeId), any(InputStream.class), eq(fileName))).thenReturn(path);

    mockMvc.perform(multipart("/api/v1/outcomes/paper/upload/{id}", outcomeId).file(mockMultipartFile))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(path));


  }

  @Test
  void test_get_oss_file_url() throws Exception {
    String outComeId = "1L";
    String testUrl = "http://minio:9000/bucket/...?signature=xyz";
    when(outcomeService.getOssFileUrl(eq(outComeId))).thenReturn(testUrl);

    mockMvc.perform(get("/api/v1/outcomes/paper/file/url").param("id", outComeId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(testUrl));
  }

  @Test
  void upload_file_no_file() throws Exception {
    mockMvc.perform(multipart("/api/v1/outcomes/paper/upload/{id}", 1L))
        .andExpect(status().isBadRequest());

  }

  @Test
  @DisplayName("正常情况：成功接收 HTTP PUT 请求，并正确反序列化 JSON 调用 Service")
  void shouldAssignCorrespondingAuthorViaHttp() throws Exception {
    // [准备阶段] 准备测试数据
    Long outcomeId = 1024L;
    List<Integer> authorIds = Arrays.asList(1, 2, 3);

    // [执行与验证阶段] 发起模拟的 HTTP 请求
    mockMvc.perform(put("/api/v1/outcomes/paper/author/{outcomeId}/corresponding-authors", outcomeId)
            .contentType(MediaType.APPLICATION_JSON) // 告诉接口，我传的是 JSON
            .content(objectMapper.writeValueAsString(authorIds))) // 把 List 变成 "[1,2,3]"

        // 1. 断言 HTTP 状态码必须是 200 OK
        .andExpect(status().isOk());

    // (可选) 如果你的 R.ok() 返回了类似 {"code": 200, "msg": "success"}，你可以继续断言：
    // .andExpect(jsonPath("$.code").value(200))

    // [验证联动] 极其关键：验证 Controller 是否老老实实地把参数交给了 Service！
    verify(outcomeService).assignCorrespondingAuthor(outcomeId, authorIds);
  }



}