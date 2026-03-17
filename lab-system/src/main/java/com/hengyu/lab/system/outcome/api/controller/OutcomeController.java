package com.hengyu.lab.system.outcome.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.outcome.application.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.application.dto.PaperMessage;
import com.hengyu.lab.system.outcome.application.dto.PaperOutcomeDTO;
import com.hengyu.lab.system.outcome.application.service.MessageService;
import com.hengyu.lab.system.outcome.application.service.PaperOutcomeQryService;
import com.hengyu.lab.system.outcome.application.service.PaperOutcomeService;
import com.hengyu.lab.system.outcome.application.query.OutcomePaperQry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController()
@RequestMapping("/api/v1/outcomes")
@Tag(name = "成果模块")
@Validated
@RequiredArgsConstructor
public class OutcomeController {

  private final MessageService messageService;
  private final PaperOutcomeQryService paperOutcomeQryService;
  private final PaperOutcomeService paperOutcomeService;


  @PostMapping("/paper")
  @Operation(summary = "添加论文") // 对应接口的描述
  public R<String> saveOutcome(@RequestBody @Valid SavePaperOutcomeCmd cmd) {
    Long id = paperOutcomeService.saveOutcome(cmd);
    return R.ok(String.valueOf(id));
  }

  @DeleteMapping("/paper/{id}")
  @Operation(summary = "删除论文")
  public R<Void> deletePaperOutcome(@PathVariable("id") String id) {
    Long outcomeId = Long.valueOf(id);
    paperOutcomeService.deleteOutcome(outcomeId);
    return R.ok();
  }

  @PostMapping("/paper/message")
  @Operation(summary = "发送爬取老师dblp元数据的消息")
  public R<Void> sendPaperMessage(@RequestBody(required = false) @Valid PaperMessage message) {
    if (message == null) {
      message = new PaperMessage();
    }
    messageService.sendPaperMessage(message);
    return R.ok();
  }

  @GetMapping()
  @Operation(summary = "查询论文成果")
  public R<IPage<PaperOutcomeDTO>> selectOutcomePage(
      @ParameterObject OutcomePaperQry outcomePaperQry) {
    IPage<PaperOutcomeDTO> outcomeIPage = paperOutcomeQryService.selectOutcomePage(outcomePaperQry);
    return R.ok(outcomeIPage);
  }

  @PostMapping("paper/upload/{id}")
  @Operation(summary = "上传文件")
  public R<String> uploadPaperOutcome(@PathVariable("id") String id,
      @RequestParam("file") MultipartFile file)
      throws IOException {
    String originalFileName = file.getOriginalFilename();
    InputStream inputStream = file.getInputStream();
    String path = paperOutcomeService.uploadPaperFile(Long.valueOf(id), inputStream,
        originalFileName);
    return R.ok(path);
  }

  @GetMapping("paper/file/url")
  @Operation(summary = "获取文件下载连接")
  public R<String> getOssFileUrl(@RequestParam("id") String id) {
    String url = paperOutcomeService.getOssFileUrl(id);
    return R.ok(url);
  }

  @PutMapping("paper/author/{outcomeId}/corresponding-authors")
  @Operation(summary = "指定通讯作者")
  public R<Void> assignCorrespondingAuthor(@PathVariable("outcomeId") Long outcomeId,
      @RequestBody List<Integer> authorIds) {
    paperOutcomeService.assignCorrespondingAuthor(outcomeId, authorIds);
    return R.ok();
  }

}
