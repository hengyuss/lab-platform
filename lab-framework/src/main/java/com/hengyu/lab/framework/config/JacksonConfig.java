package com.hengyu.lab.framework.config;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  /**
   * 全局配置：将所有的 Long 类型统统序列化为 String 完美解决前端 JS 的大数字精度丢失问题
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return builder -> {
      // 把包装类型 Long 转为 String
      builder.serializerByType(Long.class, ToStringSerializer.instance);
      // 把基本类型 long 转为 String
      builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
    };
  }
}