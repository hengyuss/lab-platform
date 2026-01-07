package com.hengyu.lab.system.outcome.application.assembler;

import com.hengyu.lab.system.outcome.application.dto.command.AuthorDTO;
import com.hengyu.lab.system.outcome.application.dto.command.SavePaperOutcomeCmd;
import com.hengyu.lab.system.outcome.domain.PaperOutcome;
import com.hengyu.lab.system.outcome.domain.vo.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OutcomeAssembler {
    PaperOutcome toPaperDomain(SavePaperOutcomeCmd cmd);

    Author toAuthor(AuthorDTO authorDTO);
}
