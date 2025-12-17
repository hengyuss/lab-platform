package com.hengyu.lab.system.feedback.infrastructure.persistence.convert;

import com.hengyu.lab.system.feedback.application.dto.clientobject.FeedbackCO;
import com.hengyu.lab.system.feedback.domain.Feedback;
import com.hengyu.lab.system.feedback.infrastructure.persistence.po.FeedbackPO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FeedbackConverter {

  FeedbackPO toPo(Feedback feedback);

  FeedbackCO toCO(FeedbackPO feedbackPo);

  Feedback toDomain(FeedbackPO feedbackPO);
}
