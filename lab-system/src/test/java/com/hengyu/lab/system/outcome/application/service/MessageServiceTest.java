package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.system.outcome.application.dto.command.PaperMessage;
import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.vo.PaperMetaTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    ArgumentCaptor<PaperMetaTask> captor =  ArgumentCaptor.forClass(PaperMetaTask.class);

    messageService.sendPaperMessage(paperMessage);
    Mockito.verify(paperMQGateway).publishTask(captor.capture());
    PaperMetaTask value = captor.getValue();

    Assertions.assertEquals("Yong Ding", value.getTeacherName());
  }
}