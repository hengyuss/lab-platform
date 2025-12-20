package com.hengyu.lab.system.user.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.system.user.application.AuthService;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.application.dto.vo.RegisterVO;
import com.hengyu.lab.system.user.domain.constant.IdentityType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private AuthService authService;

  @Test
  void register() throws Exception {
    RegisterCmd registerCmd = new RegisterCmd();
    registerCmd.setUsername("testUsername");
    registerCmd.setPassword("testPassword");
    registerCmd.setEmail("testEmail");
    registerCmd.setMobile("testMobile");
    registerCmd.setIdentityType(IdentityType.STUDENT);
    RegisterVO registerVO = new RegisterVO();
    registerVO.setUsername("testUsername");
    registerVO.setToken("testToken");
    registerVO.setIdentityType(IdentityType.STUDENT);
    Mockito.when(authService.register(Mockito.any())).thenReturn(registerVO);

    mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerCmd)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.identityType").value(IdentityType.STUDENT.getTYPE()))
        .andExpect(jsonPath("$.data.username").value("testUsername"))
        .andExpect(jsonPath("$.data.token").value("testToken"));

  }

}