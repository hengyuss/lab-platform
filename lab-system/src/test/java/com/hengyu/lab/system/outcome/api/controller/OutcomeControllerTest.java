package com.hengyu.lab.system.outcome.api.controller;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.framework.security.TokenService;
import com.hengyu.lab.framework.utils.JwtUtils;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.application.service.MessageService;
import com.hengyu.lab.system.outcome.application.service.OutcomeService;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomeQry;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.domain.vo.PaperMessage;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
    SavePaperOutcomeCmd cmd = SavePaperOutcomeCmd.builder()
        .title("testTitle")
        .issn("testIssn")
        .type(OutcomeType.PAPER)
        .status(OutcomeStatus.DRAFT)
        .journalName("testJournal")
        .publishTime(LocalDateTime.now())
        .build();

    Mockito.when(outcomeService.saveOutcome(cmd)).thenReturn(123L);
    mockMvc.perform(post("/api/v1/outcomes/paper")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cmd)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value("123"));
  }

  @Test
  void delete_outcome() throws Exception {
    mockMvc.perform(delete("/api/v1/outcomes/paper/{id}", "1"))
        .andDo(print())
        .andExpect(status().isOk());

    verify(outcomeService).deleteOutcome(1L);
  }

  @Test
  void select_outcome_page() throws Exception {
    OutcomeQry outcomeQry = new OutcomeQry();
    PaperOutcome origin = new PaperOutcome();
    origin.setTitle("Title");
    origin.setType(OutcomeType.PAPER);
    origin.setStatus(OutcomeStatus.DRAFT);
    origin.setIssn("testIssn");
    origin.setPublishTime(LocalDateTime.now());
    origin.setAuthors(List.of(Author.builder().name("old").sort(1).isCorresponding(0).build()));

    outcomeQry.setPageNo(1);
    outcomeQry.setPageSize(10);
    IPage<Outcome> page = new Page<>();
    page.setTotal(1);
    page.setRecords(List.of(origin));
    Mockito.when(outcomeService.selectOutcomePage(outcomeQry)).thenReturn(page);

    mockMvc.perform(get("/api/v1/outcomes")
            .param("pageNo", "1")
            .param("pageSize", "10"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.total").value(1))
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
    mockMvc.perform(post("/api/v1/outcomes/paper/message")
            .contentType(MediaType.APPLICATION_JSON) // 设置 Header: Content-Type
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
  @DisplayName("发送消息失败：当参数缺失时，应返回 400 Bad Request")
  void sendPaperMessage_ShouldReturn400_WhenInputIsInvalid() throws Exception {
    // --- 1. 准备非法数据 (假设 TeacherName 有 @NotNull) ---
    PaperMessage invalidDto = new PaperMessage();
    invalidDto.setTeacherName(null); // ❌ 故意传空

    // --- 2. 执行请求 ---
    mockMvc.perform(post("/api/v1/outcomes/paper/message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidDto)))

        // --- 3. 验证 ---
        .andExpect(status().isBadRequest()); // 期望 HTTP 400

    // 确保 Service 方法**没有**被调用 (因为参数校验这一关就没过)
    // verify(messageService, never()).sendPaperMessage(any());
  }


}