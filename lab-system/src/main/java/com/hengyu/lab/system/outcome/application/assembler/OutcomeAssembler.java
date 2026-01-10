package com.hengyu.lab.system.outcome.application.assembler;

import com.hengyu.lab.system.outcome.application.dto.command.AuthorDTO;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OutcomeAssembler {

    @Mapping(source = "authorList", target = "authors")
    PaperOutcome toPaperDomain(SavePaperOutcomeCmd cmd);

    Author toAuthor(AuthorDTO authorDTO);
}
