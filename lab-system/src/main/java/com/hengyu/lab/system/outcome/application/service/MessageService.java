package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.vo.PaperMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

  private final PaperMQGateway paperMQGateway;

  public void sendPaperMessage(PaperMessage message) {
    paperMQGateway.publishTask(message);
  }

}
