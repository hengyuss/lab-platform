package com.hengyu.lab.common.security.handler;

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
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class RestAccessDeniedHandlerTest {

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private PrintWriter printWriter; // 关键：Mock 这个 Writer

  @InjectMocks
  private RestAccessDeniedHandler accessDeniedHandler;

  @Test
  void testHandle() throws Exception {
    // 1. 准备数据
    String requestUri = "/api/admin/secret";
    String expectedJson = "{\"code\":403,\"msg\":\"无权限\"}"; // 假设这是转换后的 JSON

    // Mock Request 路径 (为了日志不报空指针，虽然测不到日志)
    when(request.getRequestURI()).thenReturn(requestUri);

    // Mock Response.getWriter() 返回我们要监控的 printWriter
    when(response.getWriter()).thenReturn(printWriter);

    // Mock ObjectMapper 的行为
    // 这里不管 R.fail 返回啥，我们要验证 handler 是否把 mapper 转换的结果写出去了
    when(objectMapper.writeValueAsString(any())).thenReturn(expectedJson);

    // 2. 执行测试
    accessDeniedHandler.handle(request, response, new AccessDeniedException("无权访问"));

    // 3. 验证 (Verify)

    // 验证状态码是否设为了 200 (注意：Spring Security 默认是 403，你这里强制改成了 200)
    verify(response).setStatus(HttpServletResponse.SC_OK);

    // 验证 Content-Type
    verify(response).setContentType("application/json;charset=UTF-8");

    // 验证 flush() 是否被调用 (try-with-resources 会自动 close/flush，验证 write 即可)
    verify(printWriter).write(expectedJson);
  }

}