package com.hengyu.lab.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("实验室公共服务平台接口文档")
            .version("0.0.1-SNAPSHOT")
            .description("实验室公共服务平台")
            .contact(new Contact()
                .name("Hengyu") // 你的名字
                .email("1183660933@qq.com")));
  }
}
