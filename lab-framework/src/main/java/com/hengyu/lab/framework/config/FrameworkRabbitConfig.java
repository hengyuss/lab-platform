package com.hengyu.lab.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class FrameworkRabbitConfig {


  @Bean
  public MessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();

    // 这一行告诉 Jackson：序列化和反序列化时，自动把 "teacherName" <-> "teacher_name" 互转
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    return new Jackson2JsonMessageConverter(objectMapper);
  }


  @Bean
  @Primary
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter());

    rabbitTemplate.setMandatory(true);

    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
      if (!ack) {
        log.error("消息未到达交换机 {}", cause);
      }
    });

    rabbitTemplate.setReturnsCallback(returned -> {
      log.error("消息未到达队列 RoutineKey {}, 原因 {}", returned.getRoutingKey(),
          returned.getReplyText());
    });

    return rabbitTemplate;
  }


  @Bean
  @Primary
  public SimpleRabbitListenerContainerFactory fastFactory(ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory simpleFactory = new SimpleRabbitListenerContainerFactory();
    simpleFactory.setConnectionFactory(connectionFactory);
    simpleFactory.setMessageConverter(messageConverter());

    simpleFactory.setPrefetchCount(20);
    simpleFactory.setConcurrentConsumers(5);
    simpleFactory.setMaxConcurrentConsumers(10);
    simpleFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

    return simpleFactory;
  }


}
