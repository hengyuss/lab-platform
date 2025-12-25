package com.hengyu.lab.common.security.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class ResultAuthenticationEntryPointTest {


  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private PrintWriter printWriter; // 模拟 Response 的输出流

  @InjectMocks
  private ResultAuthenticationEntryPoint entryPoint;

  @Test
  void testCommence() throws Exception {
    // 1. 准备数据 (Given)
    String requestUri = "/api/protected/resource";
    // 假设 ObjectMapper 将 R 对象转换成了这个字符串
    String expectedJson = "{\"code\":401,\"msg\":\"未认证\"}";

    // 模拟 AuthenticationException (Spring Security 抛出的异常)
    AuthenticationException authException = new InsufficientAuthenticationException("Token无效");

    // Mock 行为
    when(request.getRequestURI()).thenReturn(requestUri); // 防止日志打印报错
    when(response.getWriter()).thenReturn(printWriter);   // 获取流
    when(objectMapper.writeValueAsString(any())).thenReturn(expectedJson); // 模拟 JSON 转换

    // 2. 执行测试 (When)
    entryPoint.commence(request, response, authException);

    // 3. 验证结果 (Then)

    // 验证状态码是否被设置为 200 (SC_OK)
    // 注意：标准 HTTP 应该是 401，但你的代码逻辑是返回 200 并在 Body 里写 401，所以这里测 200
    verify(response).setStatus(HttpServletResponse.SC_OK);

    // 验证 Content-Type
    verify(response).setContentType("application/json;charset=UTF-8");

    // 验证是否真的把 JSON 写入了输出流
    verify(printWriter).write(expectedJson);

  }
}