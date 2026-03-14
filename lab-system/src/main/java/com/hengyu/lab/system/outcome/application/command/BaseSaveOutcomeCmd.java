package com.hengyu.lab.system.outcome.application.command;

import com.hengyu.lab.system.outcome.application.dto.AuthorDTO;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeStatus;
import com.hengyu.lab.system.outcome.domain.constant.OutcomeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
