package com.hengyu.lab.system.outcome.api.controller;

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
import com.hengyu.lab.system.outcome.application.service.OutcomeService;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.query.OutcomeQry;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import java.time.LocalDateTime;
import java.util.List;
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

    Mockito.verify(outcomeService).deleteOutcome(1L);
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
}