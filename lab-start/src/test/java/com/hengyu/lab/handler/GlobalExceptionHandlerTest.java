package com.hengyu.lab.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.common.api.ResultCode;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.security.TokenService;
import com.hengyu.lab.framework.utils.JwtUtils;
import com.hengyu.lab.system.user.domain.exception.UserResultCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestController.class})
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {


  @MockBean
  private TokenService tokenService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JwtUtils jwtUtils;

  @Validated
  @RestController
  @RequestMapping("/test/ex")
  static class TestController {
    @GetMapping("/biz")
    public void throwBiz() {
      throw new BizException(ResultCode.FAILURE.getCode(), "业务挂了");
    }

    @GetMapping("/biz-error")
    public void throwError() {
      // 抛出一个带有 cause 的异常，触发你的 log.error 分支
      throw new BizException(ResultCode.FAILURE, new IllegalArgumentException("参数不对"));
    }

    @PostMapping("/valid")
    public void throwValid(@RequestBody @Validated TestDto testDto) {
      // 方法体为空是预期的。
      // 当传入无效参数时，Spring会在进入此方法前抛出 MethodArgumentNotValidException，
      // 该异常会被 GlobalExceptionHandler 捕获。
      // 如果进入了方法体，说明参数校验通过（或者测试用例写错了）。
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

    @GetMapping("/constraint")
    public void throwConstraint(@RequestParam("age") @Min(value = 18, message = "未成年人禁止入内") Integer age) {
      // do nothing
    }

    @GetMapping("/trigger-error")
    public void trigger() {
      throw new AccessDeniedException("故意抛出的权限异常");
    }

  }

  @Data
  static class TestDto {
    @NotNull(message = "名字不能为空")
    private String name;
  }

  @Test
  @DisplayName("集成测试：当 Controller 抛出权限异常时，应拦截并返回 JSON")
  void shouldInterceptAccessDeniedException() throws Exception {
    mockMvc.perform(get("/test/ex/trigger-error")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()) // 打印请求详情，方便调试

        // 1. 验证 HTTP 状态码
        // 注意：GlobalHandler 通常返回 HTTP 200，但 JSON 里包含错误码
        // 如果你的代码里没设置 response.setStatus，这里就是 200
        .andExpect(status().isOk())

        // 2. 验证 JSON 内容
        .andExpect(jsonPath("$.code").value(ResultCode.NO_PRIVILEGE.getCode()))
        .andExpect(jsonPath("$.msg").value(ResultCode.NO_PRIVILEGE.getMsg()));
  }

  @Test
  void shouldHandleBizException() throws Exception {
    mockMvc.perform(get("/test/ex/biz-error")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) // 通常业务异常 HTTP 状态码也是 200
        .andExpect(jsonPath("$.code").value(ResultCode.FAILURE.getCode()));
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

  @Test
  void handleConstraintViolationException() throws Exception {
    // 模拟请求：age = 10 (小于 18)
    mockMvc.perform(get("/test/ex/constraint")
            .param("age", "10"))
        .andDo(print())
        .andExpect(status().isOk()) // 假设你返回 200
        .andExpect(jsonPath("$.code").value(ResultCode.ARGUMENT_NOT_VALID.getCode()))
        .andExpect(jsonPath("$.msg").value(org.hamcrest.Matchers.containsString("未成年人禁止入内")));
  }


}

