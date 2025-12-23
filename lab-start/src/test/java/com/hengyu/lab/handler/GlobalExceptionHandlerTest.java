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
import jakarta.validation.constraints.Min;
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
import org.springframework.web.bind.annotation.RequestParam;
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

  @Validated
  @RestController
  @RequestMapping("/test/ex")
  static class TestController {
    @GetMapping("/biz")
    public void throwBiz() {
      throw new BizException(ResultCode.FAILURE.getCode(), "业务挂了");
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

  @Test
  void handleConstraintViolationException() throws Exception {
    // 模拟请求：age = 10 (小于 18)
    mockMvc.perform(get("/test/ex/constraint")
            .param("age", "10"))
        .andDo(print())
        .andExpect(status().isOk()) // 假设你返回 200
        .andExpect(jsonPath("$.code").value(ResultCode.ARGUMENT_NOT_VALID.getCode()))
        // ⚠️ 注意：默认的 e.getMessage() 返回的格式通常是 "方法名.参数名: 错误信息"
        // 例如: "throwConstraint.age: 未成年人禁止入内"
        // 你可以用 containsString 来断言
        .andExpect(jsonPath("$.msg").value(org.hamcrest.Matchers.containsString("未成年人禁止入内")));
  }


}

