package com.hengyu.lab.system.outcome.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.framework.security.TokenService;
import com.hengyu.lab.framework.utils.JwtUtils;
import com.hengyu.lab.system.outcome.application.OutcomeService;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}