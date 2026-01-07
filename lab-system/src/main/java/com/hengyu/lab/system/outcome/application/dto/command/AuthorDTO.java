package com.hengyu.lab.system.outcome.application.dto.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDTO {
    private String name;
    private Integer sort;
    private Integer isCorresponding;
}
