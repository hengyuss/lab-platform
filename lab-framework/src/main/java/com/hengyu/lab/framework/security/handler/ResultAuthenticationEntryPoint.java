package com.hengyu.lab.framework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.common.api.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResultAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException{
      log.warn("[安全] 拦截未认证请求: Path = {}, Msg = {}", request.getRequestURI(), authException.getMessage());
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("application/json;charset=UTF-8");

      try (PrintWriter out = response.getWriter()) {
        out.write(objectMapper.writeValueAsString(R.fail(ResultCode.UN_AUTHORIZED)));
      }
  }
}
