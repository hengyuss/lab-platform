package com.hengyu.lab.system.outcome.infrastructure.convert;

import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import com.hengyu.lab.system.outcome.infrastructure.mq.dto.PaperMetaResult.PaperItemDTO;
import com.hengyu.lab.system.outcome.infrastructure.po.OutcomePO;
import com.hengyu.lab.system.outcome.infrastructure.po.PaperOutcomePO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {OutcomeType.class, OutcomeStatus.class})
public interface PaperOutcomeConverter {

  @Mapping(source = "id", target = "outcomeId")
  PaperOutcomePO toPO(PaperOutcome paperOutcome);

  @Mapping(source = "outcomePO.id", target = "id")
  PaperOutcome toDomain(OutcomePO outcomePO, PaperOutcomePO paperOutcomePO);

  @Mapping(source = "venue", target = "journalName")
  @Mapping(source = "title", target = "title")
  @Mapping(source = "type", target = "paperType")
  @Mapping(target = "type", expression = "java(OutcomeType.PAPER)")
  @Mapping(target = "status", expression = "java(OutcomeStatus.PUBLISHED)")
  PaperOutcome toDomain(PaperItemDTO paperItemDTO);

  default List<Author> mapToAuthors(List<String> authors) {
    List<Author> authorList = new ArrayList<>();
    Integer notCorresponding = 0;
    for (int i = 0; i < authors.size(); i++) {
      String name = authors.get(i);
      int sort = i + 1;
      Author author = Author.builder().name(name).sort(sort).isCorresponding(notCorresponding).build();
      authorList.add(author);
    }
    return authorList;
  }
}