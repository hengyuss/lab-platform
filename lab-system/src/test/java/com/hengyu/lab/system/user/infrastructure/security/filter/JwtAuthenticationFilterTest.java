package com.hengyu.lab.system.user.infrastructure.security.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
  private UserDetails userDetails;

  @Mock
  private io.jsonwebtoken.Claims claims;

  @InjectMocks
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void testDoFilterInternal_NoHeader() throws Exception {
    when(request.getHeader("Authorization")).thenReturn(null);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);

    assertNull(SecurityContextHolder.getContext().getAuthentication());

    verifyNoInteractions(jwtUtils);
  }

  @Test
  void testDoFilterInternal_Header_not_start_with_token_prefix() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("askdj asdlkjlkaj");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);

    assertNull(SecurityContextHolder.getContext().getAuthentication());

    verifyNoInteractions(jwtUtils);
  }

  @Test
  void testDoFilterInternal_ValidToken() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer testToken");
    when(jwtUtils.parseToken("testToken")).thenReturn(claims);
    when(claims.getSubject()).thenReturn("testUsername");
    when(userDetailsService.loadUserByUsername("testUsername")).thenReturn(userDetails);
    when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    assertNotNull(auth);
    assertEquals(userDetails, auth.getPrincipal());
    assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
  }

  @Test
  void token_parse_fail() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer testToken");
    when(jwtUtils.parseToken("testToken")).thenThrow(new RuntimeException("testToken parse fail"));

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }




}