package com.hengyu.lab.system.outcome.application.dto.command;

import com.hengyu.lab.system.outcome.domain.constants.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constants.OutcomeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BaseSaveOutcomeCmd {

    @Schema(description = "成果标题")
    @NotBlank(message = "成果标题不能为空")
    private String title;

    @Schema(description = "成果类型")
    @NotNull(message = "成果类型不能为空")
    private OutcomeType type;


    @Schema(description = "成果状态")
    @NotNull(message = "成果状态不能为空")
    private OutcomeStatus status;

    @Schema(description = "作者列表")
    private List<AuthorDTO> authorList;

}
