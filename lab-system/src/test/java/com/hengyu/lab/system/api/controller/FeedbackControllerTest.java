package com.hengyu.lab.system.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.system.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.application.service.FeedbackAppService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}