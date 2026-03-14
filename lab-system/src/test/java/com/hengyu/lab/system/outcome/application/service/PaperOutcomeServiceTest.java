package com.hengyu.lab.system.outcome.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.oss.OssTemplate;
import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
import com.hengyu.lab.system.outcome.domain.repository.PaperOutcomeRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaperOutcomeServiceTest {

  @Mock
  private PaperOutcomeRepository outcomeRepository;

  @Mock
  private OssTemplate ossTemplate;

  @InjectMocks
  private PaperOutcomeService outcomeService;

  @Captor
  private ArgumentCaptor<PaperOutcome> outcomeArgumentCaptor;


  @Test
  void test_delete_outcome_success() {
    PaperOutcome paperOutcome = PaperOutcome.builder()
        .id(1L)
        .title("testTitle")
        .issn("testIssn")
        .type(OutcomeType.PAPER)
        .status(OutcomeStatus.DRAFT)
        .journalName("testJournal")
        .publishTime(LocalDateTime.now())
        .build();

    when(outcomeRepository.findById(1L)).thenReturn(Optional.of(paperOutcome));
    outcomeService.deleteOutcome(1L);

    verify(outcomeRepository).findById(1L);
    verify(outcomeRepository).delete(paperOutcome);
  }

  @Test
  void test_delete_outcome_fail() {
    when(outcomeRepository.findById(1L))
        .thenThrow(new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    Assertions.assertThatThrownBy(() -> outcomeService.deleteOutcome(1L))
        .isInstanceOf(BizException.class);
    verify(outcomeRepository, never()).delete(any(PaperOutcome.class));
  }


  @Test
  void test_upload_file_success() throws IOException {

    String path = "testPath";
    String file = "testFile";
    String fileName = "testFileName";
    InputStream inputStream = new ByteArrayInputStream(file.getBytes());

    PaperOutcome outcome = PaperOutcome.builder().id(1L).title("testTitle").issn("testIssn")
        .build();
    when(outcomeRepository.findById(1L)).thenReturn(Optional.of(outcome));
    when(ossTemplate.uploadFile(1L, inputStream, fileName)).thenReturn(path);

    outcomeService.uploadPaperFile(1L, inputStream, fileName);
    verify(outcomeRepository).save(outcomeArgumentCaptor.capture());
    String getPath = outcomeArgumentCaptor.getValue().getOssPath();
    Assertions.assertThat(getPath).isEqualTo(path);

  }

  @Test
  void test_upload_file_fail() throws IOException {

    String path = "testPath";
    String file = "testFile";
    String fileName = "testFileName";
    InputStream inputStream = new ByteArrayInputStream(file.getBytes());

    when(outcomeRepository.findById(1L))
        .thenThrow(new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    when(ossTemplate.uploadFile(1L, inputStream, fileName)).thenReturn(path);

    Assertions.assertThatThrownBy(() -> outcomeService.uploadPaperFile(1L, inputStream, fileName))
        .isInstanceOf(BizException.class);
    verify(outcomeRepository, never()).save(any(PaperOutcome.class));


  }

  @Test
  void test_get_oss_file_url_success() throws IOException {
    PaperOutcome paperOutcome = PaperOutcome.builder()
        .title("testTitle")
        .issn("testIssn")
        .type(OutcomeType.PAPER)
        .status(OutcomeStatus.DRAFT)
        .journalName("testJournal")
        .ossPath("testPath")
        .publishTime(LocalDateTime.now())
        .build();

    when(outcomeRepository.findById(eq(1L))).thenReturn(Optional.of(paperOutcome));
    when(ossTemplate.getPresignedUrl(eq("testPath"))).thenReturn("testUrl");

    outcomeService.getOssFileUrl("1");

    verify(ossTemplate).getPresignedUrl(eq("testPath"));
    verify(outcomeRepository).findById(eq(1L));
  }

  @Test
  void test_get_oss_file_url_fail() throws IOException {
    when(outcomeRepository.findById(eq(1L))).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> outcomeService.getOssFileUrl("1"))
        .isInstanceOf(BizException.class);
  }


  @Test
  @DisplayName("正常情况：成功找到论文，设置通讯作者并保存")
  void shouldAssignCorrespondingAuthorSuccessfully() {
    // [准备阶段]
    Long outcomeId = 1L;
    List<Integer> authorIds = Arrays.asList(1, 2);
    PaperOutcome mockPaper = new PaperOutcome();
    // 告诉假仓库：当有人拿着 ID 1 来找你时，你就把这篇假论文给他！
    when(outcomeRepository.findById(outcomeId)).thenReturn(Optional.of(mockPaper));

    // [执行阶段]
    outcomeService.assignCorrespondingAuthor(outcomeId, authorIds);

    // [验证阶段] (极其关键)
    // 1. 验证假仓库的 save 方法确实被调用了 1 次，并且存进去的是我们那篇论文
    verify(outcomeRepository, times(1)).save(mockPaper);
    // (注：至于 assignCorresponding 内部逻辑对不对，那是上一个领域层测试负责的，这里不测)
  }

  @Test
  @DisplayName("异常情况：数据库里根本找不到这篇成果，抛出业务异常")
  void shouldThrowExceptionWhenOutcomeNotFound() {
    // [准备阶段]
    Long outcomeId = 99L;
    List<Integer> authorIds = Arrays.asList(1, 2);
    // 告诉假仓库：查无此文！返回空
    when(outcomeRepository.findById(outcomeId)).thenReturn(Optional.empty());

    // [执行与验证阶段]
    // 期待 Service 抛出 BizException 异常
    assertThatThrownBy(() -> outcomeService.assignCorrespondingAuthor(outcomeId, authorIds))
        .isInstanceOf(BizException.class);
    // 如果你想校验错误码，可以接着写：.hasMessageContaining("OUTCOME_NOT_FOUND")

    // 🌟 防御性验证：确保存储动作绝对没有被执行！
    verify(outcomeRepository, never()).save(any());
  }

}