package com.hengyu.lab.system.outcome.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqPaperConfig {

  public static final String PAPER_META_QUEUE = "paper_meta_queue";
  public static final String PAPER_META_EXCHANGE = "paper_meta_exchange";
  public static final String PAPER_META_RESULT_QUEUE = "paper_meta_result_queue";
  public static final String PAPER_META_ROUTING_KEY = "paper.meta";
  public static final String PAPER_META_RESULT_ROUTING_KEY = "paper.meta.result";

  @Bean
  public Queue paperMetaQueue() {
    return QueueBuilder.durable(PAPER_META_QUEUE).build();
  }

  @Bean
  public Queue paperMetaResultQueue() {
    return QueueBuilder.durable(PAPER_META_RESULT_QUEUE).build();
  }

  @Bean
  public DirectExchange paperMetaExchange() {
    return new DirectExchange(PAPER_META_EXCHANGE);
  }

  @Bean
  public Binding paperMetaResultBinding(DirectExchange paperMetaExchange) {
   return BindingBuilder.bind(paperMetaResultQueue()).to(paperMetaExchange).with(PAPER_META_RESULT_ROUTING_KEY);
  }

  @Bean
  public Binding paperMetaBinding(DirectExchange paperMetaExchange) {
    return BindingBuilder.bind(paperMetaQueue()).to(paperMetaExchange).with(PAPER_META_ROUTING_KEY);
  }

}
