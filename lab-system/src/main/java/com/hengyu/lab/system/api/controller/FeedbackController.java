package com.hengyu.lab.system.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.application.dto.command.CreateFeedbackCmd;
import com.hengyu.lab.system.application.dto.command.DeleteFeedbackCmd;
import com.hengyu.lab.system.application.dto.query.FeedbackQry;
import com.hengyu.lab.system.application.service.FeedbackAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/feedbacks")
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

  @GetMapping
  @Operation(summary = "获取需求分页数据")
  public R<IPage<FeedbackCO>> getFeedbackPage(@ParameterObject FeedbackQry feedbackQry){
    IPage<FeedbackCO> feedbackPage = feedbackAppService.getFeedbackPage(feedbackQry);
    return R.ok(feedbackPage);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "根据ID 删除对应需求")
  public R<Boolean> delete(@PathVariable("id") Long id){
    DeleteFeedbackCmd deleteFeedbackCmd = new DeleteFeedbackCmd();
    deleteFeedbackCmd.setId(id);
    Boolean delete = feedbackAppService.delete(deleteFeedbackCmd);
    return R.ok(delete);
  }

}
