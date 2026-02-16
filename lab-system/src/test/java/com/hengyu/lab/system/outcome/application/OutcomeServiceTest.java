package com.hengyu.lab.system.outcome.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.framework.oss.OssTemplate;
import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.application.dto.command.AuthorDTO;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.application.service.OutcomeService;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.exception.OutcomeResultCode;
import com.hengyu.lab.system.outcome.domain.query.OutcomePaperQry;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OutcomeServiceTest {

  @Mock
  private OutcomeRepository outcomeRepository;

  @Mock
  private OssTemplate ossTemplate;

  @Mock
  private OutcomeAssembler assembler;
  @InjectMocks
  private OutcomeService outcomeService;

  @Captor
  private ArgumentCaptor<Outcome> outcomeArgumentCaptor;


  @Test
  void test_save_outcome() {
    AuthorDTO authorCmd = AuthorDTO.builder()
        .name("testName")
        .sort(1)
        .isCorresponding(0)
        .build();

    SavePaperOutcomeCmd cmd = SavePaperOutcomeCmd.builder()
        .title("testTitle")
        .issn("testIssn")
        .type(OutcomeType.PAPER)
        .status(OutcomeStatus.DRAFT)
        .journalName("testJournal")
        .publishTime(LocalDateTime.now())
        .authorList(List.of(authorCmd))
        .build();

    PaperOutcome paperOutcome = PaperOutcome.builder()
        .title("testTitle")
        .issn("testIssn")
        .type(OutcomeType.PAPER)
        .status(OutcomeStatus.DRAFT)
        .journalName("testJournal")
        .publishTime(LocalDateTime.now())
        .build();
    Mockito.when(assembler.toPaperDomain(cmd)).thenReturn(paperOutcome);
    outcomeService.saveOutcome(cmd);

    Mockito.verify(outcomeRepository).save(outcomeArgumentCaptor.capture());
    Outcome savedOutcome = outcomeArgumentCaptor.getValue();
    Assertions.assertThat(savedOutcome).isInstanceOf(PaperOutcome.class);
    Assertions.assertThat(savedOutcome.getTitle()).isEqualTo("testTitle");
    Assertions.assertThat(savedOutcome.getAuthors()).isNull();
  }

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

    Mockito.when(outcomeRepository.findById(1L)).thenReturn(Optional.of(paperOutcome));
    outcomeService.deleteOutcome(1L);

    Mockito.verify(outcomeRepository).findById(1L);
    Mockito.verify(outcomeRepository).delete(paperOutcome);
  }

  @Test
  void test_delete_outcome_fail() {
    Mockito.when(outcomeRepository.findById(1L))
        .thenThrow(new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    Assertions.assertThatThrownBy(() -> outcomeService.deleteOutcome(1L))
        .isInstanceOf(BizException.class);
    Mockito.verify(outcomeRepository, Mockito.never()).delete(any(Outcome.class));
  }

  @Test
  void test_select_outcome_by_page() {
    OutcomePaperQry qry = new OutcomePaperQry();
    Page<Outcome> page = new Page<>();
    page.setTotal(3);
    page.setPages(3);
    Mockito.when(outcomeRepository.selectOutcomePage(qry)).thenReturn(page);
    IPage<Outcome> outcomeIPage = outcomeService.selectOutcomePage(qry);
    Assertions.assertThat(outcomeIPage.getPages()).isEqualTo(page.getPages());
    Assertions.assertThat(outcomeIPage.getTotal()).isEqualTo(page.getTotal());
  }

  @Test
  void test_upload_file_success() throws IOException {

    String path = "testPath";
    String file = "testFile";
    String fileName = "testFileName";
    InputStream inputStream = new ByteArrayInputStream(file.getBytes());

    PaperOutcome outcome = PaperOutcome.builder().id(1L).title("testTitle").issn("testIssn")
        .build();
    Mockito.when(outcomeRepository.findById(1L)).thenReturn(Optional.of(outcome));
    Mockito.when(ossTemplate.uploadFile(1L, inputStream, fileName)).thenReturn(path);

    outcomeService.uploadPaperFile(1L, inputStream, fileName);
    Mockito.verify(outcomeRepository).save(outcomeArgumentCaptor.capture());
    String getPath = outcomeArgumentCaptor.getValue().getOssPath();
    Assertions.assertThat(getPath).isEqualTo(path);

  }

  @Test
  void test_upload_file_fail() throws IOException {

    String path = "testPath";
    String file = "testFile";
    String fileName = "testFileName";
    InputStream inputStream = new ByteArrayInputStream(file.getBytes());

    Mockito.when(outcomeRepository.findById(1L))
        .thenThrow(new BizException(OutcomeResultCode.OUTCOME_NOT_FOUND));
    Mockito.when(ossTemplate.uploadFile(1L, inputStream, fileName)).thenReturn(path);

    Assertions.assertThatThrownBy(() -> outcomeService.uploadPaperFile(1L, inputStream, fileName));
    Mockito.verify(outcomeRepository, Mockito.never()).save(any(Outcome.class));


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

    Mockito.when(outcomeRepository.findById(eq(1L))).thenReturn(Optional.of(paperOutcome));
    Mockito.when(ossTemplate.getPresignedUrl(eq("testPath"))).thenReturn("testUrl");

    outcomeService.getOssFileUrl("1");

    Mockito.verify(ossTemplate).getPresignedUrl(eq("testPath"));
    Mockito.verify(outcomeRepository).findById(eq(1L));
  }

  @Test
  void test_get_oss_file_url_fail() throws IOException {
    Mockito.when(outcomeRepository.findById(eq(1L))).thenReturn(Optional.empty());
    Assertions.assertThatThrownBy(() -> outcomeService.getOssFileUrl("1")).isInstanceOf(BizException.class);
  }


}