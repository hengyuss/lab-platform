package com.hengyu.lab.system.infrastructure.persistence.convert;

import com.hengyu.lab.system.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.domain.feedback.Feedback;
import com.hengyu.lab.system.infrastructure.persistence.po.FeedbackPO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FeedbackConverter {

  FeedbackPO toPo(Feedback feedback);

  FeedbackCO toCO(FeedbackPO feedbackPo);

  Feedback toDomain(FeedbackPO feedbackPO);
}
