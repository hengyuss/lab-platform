package com.hengyu.lab.system.outcome.application.dto.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class SavePaperOutcomeCmd extends BaseSaveOutcomeCmd{

    @Schema(description = "论文期刊")
    @NotBlank(message = "论文期刊不能为空")
    private String journalName;
    @Schema(description = "")
    private String issn;
    @Schema(description = "发表时间")
    private LocalDateTime publishTime;
}
