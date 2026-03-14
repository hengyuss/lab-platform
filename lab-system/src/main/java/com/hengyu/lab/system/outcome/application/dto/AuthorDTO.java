package com.hengyu.lab.system.outcome.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private Integer id;
    private Long outcomeId;
    private String name;
    private Integer sort;
    private Integer isCorresponding;
}
