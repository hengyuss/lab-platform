package com.hengyu.lab.system.outcome.api.controller;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.outcome.application.OutcomeService;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/outcomes")
@Tag(name = "成果模块")
@Validated
@RequiredArgsConstructor
public class OutcomeController {

  private final OutcomeService outcomeService;


  @PostMapping("/paper")
  @Operation(summary = "添加论文") // 对应接口的描述
  public R<String> saveOutcome(@RequestBody @Valid SavePaperOutcomeCmd cmd) {
    Long id = outcomeService.saveOutcome(cmd);
    return R.ok(String.valueOf(id));
  }

  @DeleteMapping("/paper/{id}")
  @Operation(summary = "删除论文")
  public R<Void> deletePaperOutcome(@PathVariable("id") String id) {
    Long outcomeId = Long.valueOf(id);
    outcomeService.deleteOutcome(outcomeId);
    return R.ok();
  }

}
