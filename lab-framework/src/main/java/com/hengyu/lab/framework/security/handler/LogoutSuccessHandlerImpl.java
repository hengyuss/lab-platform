package com.hengyu.lab.framework.security.handler;

import com.alibaba.fastjson2.JSON;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.framework.security.AuthUser;
import com.hengyu.lab.framework.security.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

  @Autowired
  private TokenService tokenService;


  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    {
      AuthUser loginUser = tokenService.getUser(request);
      if (!Objects.isNull(loginUser)) {
        // 删除用户缓存记录
        tokenService.delLoginUser(loginUser.getUniqueKey());
      }
      response.setStatus(200);
      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");
      response.getWriter().print(JSON.toJSONString(R.ok("logout success")));
    }

  }
}
