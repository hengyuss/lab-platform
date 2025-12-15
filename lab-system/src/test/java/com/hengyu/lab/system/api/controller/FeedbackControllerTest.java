package com.hengyu.lab.system.api.controller;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.system.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.dto.command.DeleteFeedbackCmd;
import com.hengyu.lab.system.application.dto.command.UpdateFeedbackCmd;
import com.hengyu.lab.system.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.application.service.FeedbackAppService;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FeedbackController.class)
class FeedbackControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private FeedbackAppService feedbackAppService;


  @Test
  void create_success() throws Exception {
    CreateFeedbackCmd cmd = new CreateFeedbackCmd();
    cmd.setContent("testContetn");
    cmd.setTitle("testTitle");

    when(feedbackAppService.createFeedback(Mockito.any(CreateFeedbackCmd.class))).thenReturn(100L);
    mockMvc.perform(post("/api/v1/feedbacks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cmd)))
        .andDo(print())
        .andExpect(status().isOk());

  }

  @Test
  void query_success() throws Exception {
    FeedbackCO feedbackCO = new FeedbackCO();
    feedbackCO.setContent("testContetn");
    feedbackCO.setTitle("testTitle");
    IPage<FeedbackCO> page = new Page<>();
    page.setTotal(10);
    page.setRecords(List.of(feedbackCO));
    when(feedbackAppService.getFeedbackPage(Mockito.any(FeedbackQry.class))).thenReturn(page);

    mockMvc.perform(get("/api/v1/feedbacks")
            .param("pageNo", "1")
            .param("pageSize", "10"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.total").value(10))
        .andExpect(jsonPath("$.data.records[0].title").value("testTitle"));

  }

  @Test
  void delete_success() throws Exception {

    when(feedbackAppService.delete(Mockito.any(DeleteFeedbackCmd.class))).thenReturn(true);

    mockMvc.perform(delete("/api/v1/feedbacks/{id}", "1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(true));

    Mockito.verify(feedbackAppService).delete(Mockito.argThat(cmd ->
        cmd.getId().equals("1") // 验证 Service 收到的命令里，ID 确实是 1
    ));
  }

  @Test
  void delete_fail() throws Exception {

    when(feedbackAppService.delete(Mockito.any(DeleteFeedbackCmd.class))).thenReturn(false);

    mockMvc.perform(delete("/api/v1/feedbacks/{id}", "1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").value(false));

    Mockito.verify(feedbackAppService).delete(Mockito.argThat(cmd ->
        cmd.getId().equals("1") // 验证 Service 收到的命令里，ID 确实是 1
    ));
  }

  @Test
  void update_success() throws Exception {
    UpdateFeedbackCmd cmd = new UpdateFeedbackCmd();
    cmd.setId("1");
    cmd.setStatus(FeedbackStatus.SOLVING);
    mockMvc.perform(put("/api/v1/feedbacks")
        .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cmd)))
            .andDo(print());


    Mockito.verify(feedbackAppService).updateStatus(refEq(cmd));
  }

}