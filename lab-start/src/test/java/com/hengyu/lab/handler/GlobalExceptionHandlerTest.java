package com.hengyu.lab.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.common.utils.JwtUtils;
import com.hengyu.lab.system.user.domain.exception.UserResultCode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestController.class})
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtUtils jwtUtils;

  @RestController
  @RequestMapping("/test/ex")
  static class TestController {
    @GetMapping("/biz")
    public void throwBiz() {
      throw new BizException(ResultCode.FAILURE.getCode(), "业务挂了");
    }

    @PostMapping("/valid")
    public void throwValid(@RequestBody @Validated TestDto testDto) {
    }

    @GetMapping("/illegal")
    public void throwIllegal() {
      throw new IllegalArgumentException("参数非法");
    }

    @GetMapping("/bad-credentials")
    public void throwBadCredentials() {
      throw new BadCredentialsException("密码或用户名错误");
    }

    @GetMapping("/unknow")
    public void throwUnknow() {
      throw new NullPointerException("发生什么事了");
    }

  }

  @Data
  static class TestDto {
    @NotNull(message = "名字不能为空")
    private String name;
  }


  @Test
  void test_throw_biz() throws Exception {
    mockMvc.perform(get("/test/ex/biz"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.FAILURE.getCode()))
        .andExpect(jsonPath("$.msg").value("业务挂了"));
  }

  @Test
  void test_throw_method_argument_not_valid() throws Exception {
    TestDto testDto = new TestDto();


    mockMvc.perform(post("/test/ex/valid")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(testDto)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.ARGUMENT_NOT_VALID.getCode()))
        .andExpect(jsonPath("$.msg").value("名字不能为空"));
  }

  @Test
  void test_illegal_argument() throws Exception {
   mockMvc.perform(get("/test/ex/illegal"))
       .andDo(print())
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.code").value(ResultCode.ARGUMENT_NOT_VALID.getCode()))
       .andExpect(jsonPath("$.msg").value("参数非法"));
  }

  @Test
  void test_bad_credentials() throws Exception {
    mockMvc.perform(get("/test/ex/bad-credentials"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(UserResultCode.USERNAME_OR_PASSWORD_ERROR.getCode()))
        .andExpect(jsonPath("$.msg").value("密码或用户名错误"));
  }

  @Test
  void test_unknown() throws Exception {
    mockMvc.perform(get("/test/ex/unknow"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(ResultCode.FAILURE.getCode()))
        .andExpect(jsonPath("$.msg").value("系统繁忙,请稍候再试"));
  }


}

