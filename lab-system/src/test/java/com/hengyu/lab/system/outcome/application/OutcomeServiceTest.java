package com.hengyu.lab.system.outcome.application;

import com.hengyu.lab.system.outcome.application.assembler.OutcomeAssembler;
import com.hengyu.lab.system.outcome.application.dto.command.AuthorDTO;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.Outcome;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;
import com.hengyu.lab.system.outcome.domain.repository.OutcomeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OutcomeServiceTest {

    @Mock
    private OutcomeRepository outcomeRepository;

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
}