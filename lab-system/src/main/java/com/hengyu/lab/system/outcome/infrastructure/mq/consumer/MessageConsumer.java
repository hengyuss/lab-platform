package com.hengyu.lab.system.outcome.infrastructure.mq.consumer;


import com.hengyu.lab.system.outcome.infrastructure.config.RabbitmqPaperConfig;
import com.hengyu.lab.system.outcome.infrastructure.mq.dto.PaperMetaResult;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {


  @RabbitListener(queues = RabbitmqPaperConfig.PAPER_META_RESULT_QUEUE)
  public void handleMessage(PaperMetaResult result, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {

    try{
      log.info("消息:{}", result);
      channel.basicAck(tag, false);
    } catch (Exception e){
      log.error("{} 消息消费异常\n, exception: {}", result, e.getMessage());
    }
  }

}
