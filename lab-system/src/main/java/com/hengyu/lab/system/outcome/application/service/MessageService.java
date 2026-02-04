package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.system.outcome.application.dto.command.PaperMessage;
import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.vo.PaperMetaTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

  private final PaperMQGateway paperMQGateway;

  public void sendPaperMessage(PaperMessage message) {
    PaperMetaTask task = new PaperMetaTask();
    BeanUtils.copyProperties(message, task);
    paperMQGateway.publishTask(task);
  }

}
