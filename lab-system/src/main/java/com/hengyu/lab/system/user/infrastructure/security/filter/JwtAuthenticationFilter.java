package com.hengyu.lab.system.user.infrastructure.security.filter;

import com.hengyu.lab.common.utils.JwtUtils;
import com.hengyu.lab.system.user.infrastructure.security.AuthUser;
import com.hengyu.lab.system.user.infrastructure.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;
  private final JwtUtils jwtUtils;
  //TODO 使用tokenservice
  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    AuthUser user = tokenService.getUser(request);
    if (!Objects.isNull(user) && SecurityContextHolder.getContext().getAuthentication() == null) {
      tokenService.verifyToken(user);
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          user, null, user.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authToken);
      log.debug("用户 [{}] 通过认证", user.getUsername());
    }

    filterChain.doFilter(request, response);
  }

}
