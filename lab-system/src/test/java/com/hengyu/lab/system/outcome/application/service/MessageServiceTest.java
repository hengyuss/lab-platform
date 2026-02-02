package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.vo.PaperMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @Mock
  private PaperMQGateway paperMQGateway;
  @InjectMocks
  private MessageService messageService;

  @Test
  void sendPaperMessage() {
    PaperMessage paperMessage = new PaperMessage();
    paperMessage.setTeacherName("Yong Ding");

    messageService.sendPaperMessage(paperMessage);
    Mockito.verify(paperMQGateway).publishTask(ArgumentMatchers.refEq(paperMessage));
  }
}