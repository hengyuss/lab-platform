package com.hengyu.lab.system.user.infrastructure.security.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.utils.JwtUtils;
import com.hengyu.lab.system.user.domain.User;
import com.hengyu.lab.system.user.infrastructure.security.AuthUser;
import com.hengyu.lab.system.user.infrastructure.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @Mock
  private UserDetailsService userDetailsService;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @Mock
  private TokenService tokenService;

  @Mock
  private io.jsonwebtoken.Claims claims;

  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }


  @Test
  void testDoFilterInternal_ValidToken() throws Exception {
    User testUser = User.builder().username("testUsername")
        .id(100L)
        .build();
    AuthUser authUser = AuthUser.builder()
        .user(testUser)
        .build();
    when(tokenService.getUser(request)).thenReturn(authUser);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(auth);
    assertEquals(authUser, auth.getPrincipal());
    assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
  }

  @Test
  void token_parse_fail() throws Exception {

    when(tokenService.getUser(request)).thenReturn(null);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }


  @Test
  void doFilterInternal_ShouldSkip_WhenAlreadyAuthenticated() throws ServletException, IOException {
    // 1. 准备数据：手动往 Context 里塞一个已认证的对象
    AuthUser existingUser = AuthUser.builder().build();
    UsernamePasswordAuthenticationToken existingToken =
        new UsernamePasswordAuthenticationToken(existingUser, null, null);
    SecurityContextHolder.getContext().setAuthentication(existingToken);

    // 即使 tokenService 能解析出用户
    AuthUser newUser = AuthUser.builder().build();
    when(tokenService.getUser(request)).thenReturn(newUser);

    // 2. 执行
    jwtAuthenticationFilter.doFilter(request, response, filterChain);

    // 3. 验证
    // 3.1 验证 verifyToken 没调用 (因为 Context 不为空，&& 条件短路了)
    verify(tokenService, never()).verifyToken(any());

    // 3.2 验证 Context 里的对象还是原来的那个，没有被覆盖
    Assertions.assertSame(existingToken, SecurityContextHolder.getContext().getAuthentication());

    // 3.3 验证放行
    verify(filterChain).doFilter(request, response);
  }

}