package com.hengyu.lab.system.user.infrastructure.security.handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.system.user.infrastructure.security.AuthUser;
import com.hengyu.lab.system.user.infrastructure.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class LogoutSuccessHandlerImplTest {


  @Mock
  private TokenService tokenService;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Authentication authentication;

  @Mock
  private PrintWriter writer; // 模拟 Response 的输出流

  @InjectMocks
  private LogoutSuccessHandlerImpl logoutHandler;

  /**
   * 测试场景 1：Token 有效，能解析出用户
   * 预期：执行 Redis 删除操作，并返回成功消息
   */
  @Test
  void onLogoutSuccess_ShouldDeleteToken_WhenUserExists() throws IOException {
    // 1. 准备数据
    String userKey = "login_user:100";
    AuthUser mockUser = AuthUser.builder().uniqueKey(userKey).build(); // 假设你的 AuthUser 有 Builder

    // 2. 录制行为 (Stubbing)
    when(tokenService.getUser(request)).thenReturn(mockUser); // 模拟能取到用户
    when(response.getWriter()).thenReturn(writer); // 模拟 response.getWriter() 不报错

    // 3. 执行测试
    try {
      logoutHandler.onLogoutSuccess(request, response, authentication);
    } catch (Exception e) {
      // 忽略 ServletException
    }

    // 4. 验证 (Verify)
    // 4.1 验证核心逻辑：Redis 删除方法必须被调用
    verify(tokenService).delLoginUser(userKey);

    // 4.2 验证响应头设置
    verify(response).setStatus(200);
    verify(response).setContentType("application/json");

    // 4.3 验证确实写回了 JSON 数据
    // 这里验证 writer.print 被调用，且参数包含 "success" 字样
    verify(writer).print(contains("logout success"));
  }

  /**
   * 测试场景 2：Token 无效或用户已过期（获取不到 User）
   * 预期：不报错，不调用 Redis 删除，直接返回成功消息
   */
  @Test
  void onLogoutSuccess_ShouldSkipDelete_WhenUserIsNull() throws IOException {
    // 1. 录制行为：模拟取不到用户 (null)
    when(tokenService.getUser(request)).thenReturn(null);
    when(response.getWriter()).thenReturn(writer);

    // 2. 执行测试
    try {
      logoutHandler.onLogoutSuccess(request, response, authentication);
    } catch (Exception e) {
      // 忽略
    }

    // 3. 验证
    // 3.1 【关键】验证 Redis 删除方法从未被调用 (防止空指针)
    verify(tokenService, never()).delLoginUser(anyString());

    // 3.2 验证依然返回了 200 (登出通常是幂等的，原本就没登录也算登出成功)
    verify(response).setStatus(200);
    verify(writer).print(contains("logout success"));
  }


}