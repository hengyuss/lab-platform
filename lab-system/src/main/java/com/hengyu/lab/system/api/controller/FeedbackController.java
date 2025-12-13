package com.hengyu.lab.system.api.controller;

import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.service.FeedbackAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/feedback")
@Tag(name = "需求模块")
public class FeedbackController {
  @Autowired
  private FeedbackAppService feedbackAppService;

  @PostMapping
  @Operation(summary = "添加需求") // 对应接口的描述
  public R<Long> create(@RequestBody CreateFeedbackCmd cmd) {
    Long feedbackId = feedbackAppService.createFeedback(cmd);
    return R.ok(feedbackId);
  }

}
