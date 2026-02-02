package com.hengyu.lab.system.outcome.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.application.service.MessageService;
import com.hengyu.lab.system.outcome.application.service.OutcomeService;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.query.OutcomeQry;
import com.hengyu.lab.system.outcome.domain.vo.PaperMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
  private final MessageService messageService;


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

  @PostMapping("/paper/message")
  @Operation(summary = "发送爬取老师dblp元数据的消息")
  public R<Void> sendPaperMessage(@RequestBody @Valid PaperMessage message) {
    messageService.sendPaperMessage(message);
    return R.ok();
  }

  @GetMapping()
  @Operation(summary = "查询成果")
  public R<IPage<Outcome>> selectOutcomePage(@ParameterObject OutcomeQry outcomeQry){
    IPage<Outcome> outcomeIPage = outcomeService.selectOutcomePage(outcomeQry);
    return R.ok(outcomeIPage);
  }

}
