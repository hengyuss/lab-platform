package com.hengyu.lab.system.outcome.infrastructure.mq;

import com.hengyu.lab.system.outcome.domain.vo.PaperMessage;
import com.hengyu.lab.system.outcome.infrastructure.config.RabbitmqPaperConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class RabbitMQPaperGatewayTest {

  @Mock
  private RabbitTemplate rabbitTemplate;

  @InjectMocks
  private RabbitMQPaperGateway rabbitMQPaperGateway;

  @Test
  void publishTask() {
    PaperMessage message = new PaperMessage();
    message.setTeacherName("Yong Ding");
    rabbitMQPaperGateway.publishTask(message);
    Mockito.verify(rabbitTemplate).convertAndSend(
        ArgumentMatchers.eq(RabbitmqPaperConfig.PAPER_META_EXCHANGE),
        ArgumentMatchers.eq(RabbitmqPaperConfig.PAPER_META_ROUTING_KEY),
        ArgumentMatchers.refEq(message)
    );
  }

}