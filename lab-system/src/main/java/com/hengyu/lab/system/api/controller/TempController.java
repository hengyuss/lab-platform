package com.hengyu.lab.system.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/temp")
@Tag(name = "测试接口")
public class TempController {

  @GetMapping("/hello")
  @Operation(summary = "测试接口") // 对应接口的描述
  public String hello() {
    return "Hello Knife4j";
  }

}
