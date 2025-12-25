package com.hengyu.lab.common.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.common.api.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;


  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

    log.warn("[安全] 拦截越权访问: Path = {}", request.getRequestURI());
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");

    try (PrintWriter out = response.getWriter()) {
      out.write(objectMapper.writeValueAsString(
          objectMapper.writeValueAsString(R.fail(ResultCode.NO_PRIVILEGE))));
    }

  }
}
