package com.hengyu.lab.system.outcome.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private String name;
    private Integer sort;
    private Integer isCorresponding;
}
