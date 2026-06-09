package com.hengyu.lab.system.outcome.api.controller;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.outcome.application.command.SaveProjectOutcomeCmd;
import com.hengyu.lab.system.outcome.application.service.ProjectOutcomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/outcomes/projects")
@Tag(name = "项目模块")
@Validated
@RequiredArgsConstructor
public class ProjectOutcomeController {

  private final ProjectOutcomeService projectOutcomeService;


  @PostMapping
  @Operation(summary = "添加项目") // 对应接口的描述
  public R<Long> saveOutcome(@RequestBody @Valid SaveProjectOutcomeCmd cmd) {
    Long id = projectOutcomeService.saveOutcome(cmd);
    return R.ok(id);
  }


}
