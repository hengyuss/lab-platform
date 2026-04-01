package com.hengyu.lab.system.outcome.infrastructure.mq.producer;

import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.valobj.PaperMetaTask;
import com.hengyu.lab.system.outcome.infrastructure.config.RabbitmqPaperConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQPaperGateway implements PaperMQGateway {

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publishTask(PaperMetaTask message) {
    rabbitTemplate.convertAndSend(RabbitmqPaperConfig.PAPER_META_EXCHANGE,
        RabbitmqPaperConfig.PAPER_META_ROUTING_KEY, message);
    log.info("成功发送消息 {}", message);
  }

}
