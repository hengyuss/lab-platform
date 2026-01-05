package com.hengyu.lab.system.outcome.infrastructure.repository.strategy;

import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;
import com.hengyu.lab.system.outcome.infrastructure.convert.PaperOutcomeConverter;
import com.hengyu.lab.system.outcome.infrastructure.mapper.PaperOutcomeMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PaperOutcomeStrategyTest {


    @Mock
    PaperOutcomeMapper paperOutcomeMapper;

    @Mock
    PaperOutcomeConverter paperConverter;

    @InjectMocks
    PaperOutcomeStrategy outcomePaperStrategy;

    @Test
    void test_getType(){
        OutcomeType outcomeType = outcomePaperStrategy.getOutcomeType();
        Assertions.assertEquals(OutcomeType.PAPER, outcomeType);
    }

    @Test
    void test_set_details(){
        PaperOutcome paperOutcome = PaperOutcome.builder().build();
        Mockito.when(paperConverter.toPO(paperOutcome)).thenReturn(PaperOutcomePO.builder().build());
        outcomePaperStrategy.saveDetails(paperOutcome);
        Mockito.verify(paperOutcomeMapper).insert(any(PaperOutcomePO.class));
    }

}