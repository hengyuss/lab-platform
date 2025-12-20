package com.hengyu.lab.system.user.api.controller;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.user.application.AuthService;
import com.hengyu.lab.system.user.application.dto.command.RegisterCmd;
import com.hengyu.lab.system.user.application.dto.vo.RegisterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "认证中心")
@Validated
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("register")
  @Operation(summary = "注册用户") // 对应接口的描述
  public R<RegisterVO> register(@RequestBody @Validated RegisterCmd cmd) {
    RegisterVO registerVO = authService.register(cmd);
    return R.ok(registerVO);
  }

}
