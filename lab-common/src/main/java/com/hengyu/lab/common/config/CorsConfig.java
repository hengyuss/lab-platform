package com.hengyu.lab.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // 允许跨域访问的路径
        // .allowedOrigins("*") // ⚠️ 注意：如果允许携带 Cookie，不能写 *
        .allowedOriginPatterns("*") // SpringBoot 2.4+ 推荐写法，允许所有域名
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
        .allowedHeaders("*") // 允许的请求头
        .allowCredentials(true) // 是否允许发送 Cookie
        .maxAge(3600); // 预检请求(OPTIONS)的缓存时间(秒)
  }

}
