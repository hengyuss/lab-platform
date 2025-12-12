package com.hengyu.lab.system.api.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateFeedbackCmd {
  public String title;
  public String content;
}
