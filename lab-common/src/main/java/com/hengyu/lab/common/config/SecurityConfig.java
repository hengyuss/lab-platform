package com.hengyu.lab.common.config;

import com.hengyu.lab.common.filter.JwtAuthenticationFilter;
import com.hengyu.lab.common.security.handler.ResultAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter  jwtAuthenticationFilter;

  private final ResultAuthenticationEntryPoint resultAuthenticationEntryPoint;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/auth/**").permitAll()
            // 2. Knife4j / Swagger 相关的静态资源和接口 (必须全部放行)
            .requestMatchers("/doc.html").permitAll()          // Knife4j 主页
            .requestMatchers("/webjars/**").permitAll()        // 静态资源
            .requestMatchers("/v3/api-docs/**").permitAll()   // Swagger3 的 JSON 接口
//            .requestMatchers("/api/v1/feedbacks/**").permitAll()
            .requestMatchers("/swagger-resources/**").permitAll()

            // 3. 基础资源
            .requestMatchers("/favicon.ico", "/error").permitAll()
        .anyRequest().authenticated())
        .exceptionHandling(exception ->
            exception.authenticationEntryPoint(resultAuthenticationEntryPoint))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }




}
