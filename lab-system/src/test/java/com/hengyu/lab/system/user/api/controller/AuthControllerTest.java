package com.hengyu.lab.system.user.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.common.utils.JwtUtils;
import com.hengyu.lab.system.user.application.AuthService;
import com.hengyu.lab.system.user.application.dto.command.LoginCmd;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.application.dto.vo.AuthVO;
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
  @MockBean
  private JwtUtils jwtUtils;

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
    registerCmd.setRealName("testRealName");
    registerCmd.setIdentityType(IdentityType.STUDENT);
    AuthVO authVO = new AuthVO();
    authVO.setUsername("testUsername");
    authVO.setToken("testToken");
    authVO.setIdentityType(IdentityType.STUDENT);
    Mockito.when(authService.register(Mockito.any())).thenReturn(authVO);

    mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerCmd)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.identityType").value(IdentityType.STUDENT.getTYPE()))
        .andExpect(jsonPath("$.data.username").value("testUsername"))
        .andExpect(jsonPath("$.data.token").value("testToken"));
  }

  @Test
  void register_fail_when_missing_field() throws Exception {
    RegisterCmd registerCmd = new RegisterCmd();
    registerCmd.setUsername("testUsername");

    mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerCmd)))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void login() throws Exception {
    LoginCmd loginCmd = new LoginCmd();
    loginCmd.setUsername("testUsername");
    loginCmd.setPassword("testPassword");

    AuthVO authVO = new AuthVO();
    authVO.setUsername("testUsername");
    authVO.setToken("testToken");
    authVO.setIdentityType(IdentityType.STUDENT);
    Mockito.when(authService.login(loginCmd)).thenReturn(authVO);


    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginCmd)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("testUsername"))
        .andExpect(jsonPath("$.data.identityType").value(IdentityType.STUDENT.getTYPE()))
        .andExpect(jsonPath("$.data.token").value("testToken"));

  }

}