package com.hengyu.lab.system.outcome.infrastructure.mq.consumer;


import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import com.hengyu.lab.system.outcome.infrastructure.config.RabbitmqPaperConfig;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mq.dto.PaperMetaResult;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.CollectionsUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {

  private final OutcomeRepository outcomeRepository;
  private final PaperOutcomeConverter converter;
  public static int count = 0;

  @RabbitListener(queues = RabbitmqPaperConfig.PAPER_META_RESULT_QUEUE)
  public void handleMessage(PaperMetaResult result, Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

    log.info("消息:{}", result);
    if (result.getStatus() && CollectionsUtils.hasItems(result.getData())) {
      result.getData().stream()
          .filter(paperItemDTO -> !outcomeRepository.existsByDblpKey(paperItemDTO.getDblpKey()))
          .forEach(item -> {
            try {
              PaperOutcome outcome = converter.toDomain(item);
              outcomeRepository.save(outcome);
              count ++;
            } catch (Exception e) {
              log.error("title:{} dblpKey:{}入库失败\n exception: {}", item.getTitle(),
                  item.getDblpKey(), e.getMessage());
            }
          });
    }
    log.info("共有 {} 数据", count);

    channel.basicAck(tag, false);
  }

}
