package com.hengyu.lab.system.outcome.domain.service;

import com.hengyu.lab.system.outcome.domain.vo.PaperMessage;

public interface PaperMQGateway {
  public void publishTask(PaperMessage message);
}
