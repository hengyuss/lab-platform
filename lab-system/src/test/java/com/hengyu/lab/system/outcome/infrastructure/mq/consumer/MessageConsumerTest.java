package com.hengyu.lab.system.outcome.infrastructure.mq.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mq.dto.PaperMetaResult;
import com.hengyu.lab.system.outcome.infrastructure.mq.dto.PaperMetaResult.PaperItemDTO;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageConsumerTest {

  @Mock
  OutcomeRepository outcomeRepository;
  @Mock
  PaperOutcomeConverter paperOutcomeConverter;
  @Mock
  private Channel channel;

  @InjectMocks
  MessageConsumer messageConsumer;


  @Test
  void result_status_is_false() throws IOException {
    PaperMetaResult result = new PaperMetaResult();
    result.setStatus(false);
    long deliveryTag = 1l;
    result.setData(Collections.EMPTY_LIST);


    messageConsumer.handleMessage(result, channel, deliveryTag);

    verify(outcomeRepository, never()).existsByDblpKey(anyString());
    verify(outcomeRepository, never()).save(any(Outcome.class));
    verify(channel, times(1)).basicAck(deliveryTag, false);
  }

  @Test
  void success_handle_message() throws IOException {
    PaperMetaResult result = new PaperMetaResult();
    result.setStatus(true);
    result.setPid("testPid");
    result.setTeacherName("testTeacherName");
    PaperItemDTO dto = new PaperItemDTO();
    dto.setDblpKey("testDblpKey");
    dto.setEe("ee");
    dto.setType("type");
    dto.setPublishYear("2025");
    dto.setVenue("venue");
    dto.setAuthors(List.of("author1", "author2"));
    result.setData(List.of(dto));
    long deliveryTag = 1l;
    PaperOutcome paperOutcome =  new PaperOutcome();
    when(paperOutcomeConverter.toDomain(dto)).thenReturn(paperOutcome);
    when(outcomeRepository.existsByDblpKey(dto.getDblpKey())).thenReturn(false);
    messageConsumer.handleMessage(result, channel, deliveryTag);


    verify(outcomeRepository).existsByDblpKey(anyString());
    verify(outcomeRepository).save(any(Outcome.class));
    verify(channel, times(1)).basicAck(deliveryTag, false);
  }

  @Test
  void handleMessage_when_paper_exist() throws IOException {
    PaperMetaResult result = new PaperMetaResult();
    result.setStatus(true);
    result.setPid("testPid");
    result.setTeacherName("testTeacherName");
    PaperItemDTO dto = new PaperItemDTO();
    dto.setDblpKey("testDblpKey");
    dto.setEe("ee");
    dto.setType("type");
    dto.setPublishYear("2025");
    dto.setVenue("venue");
    dto.setAuthors(List.of("author1", "author2"));
    result.setData(List.of(dto));
    long deliveryTag = 1l;
    when(outcomeRepository.existsByDblpKey(dto.getDblpKey())).thenReturn(true);
    messageConsumer.handleMessage(result, channel, deliveryTag);


    verify(outcomeRepository, never()).save(any(Outcome.class));
    verify(paperOutcomeConverter, never()).toDomain(dto);
    verify(channel, times(1)).basicAck(deliveryTag, false);
  }

  @Test
  @DisplayName("场景3：异常隔离 - 第一条保存失败，不应影响第二条保存")
  void handleMessage_when_one_fail_other_success() throws IOException {
    PaperMetaResult result = new PaperMetaResult();
    result.setStatus(true);
    result.setPid("testPid");
    result.setTeacherName("testTeacherName");
    PaperItemDTO dto = new PaperItemDTO();
    dto.setDblpKey("testDblpKey");
    dto.setEe("ee");
    dto.setType("type");
    dto.setPublishYear("2025");
    dto.setVenue("venue");
    dto.setAuthors(List.of("author1", "author2"));
    PaperItemDTO dto2 = new PaperItemDTO();
    dto2.setDblpKey("testDblpKey2");
    dto2.setEe("ee2");
    dto2.setType("type2");
    dto2.setPublishYear("2026");
    dto2.setVenue("venue2");
    dto2.setAuthors(List.of("author12", "author22"));
    result.setData(List.of(dto, dto2));
    long deliveryTag = 1l;
    PaperOutcome paperOutcome1 =  new PaperOutcome();
    paperOutcome1.setDblpKey("testDblpKey");
    PaperOutcome paperOutcome2 =  new PaperOutcome();
    paperOutcome2.setDblpKey("testDblpKey2");
    when(paperOutcomeConverter.toDomain(dto)).thenReturn(paperOutcome1);
    when(paperOutcomeConverter.toDomain(dto2)).thenReturn(paperOutcome2);
    doThrow(new RuntimeException()).when(outcomeRepository).save(paperOutcome1);
    doNothing().when(outcomeRepository).save(paperOutcome2);
    messageConsumer.handleMessage(result, channel, deliveryTag);

    verify(outcomeRepository).save(paperOutcome1);
    verify(outcomeRepository).save(paperOutcome2);
    verify(channel, times(1)).basicAck(deliveryTag, false);


  }



}