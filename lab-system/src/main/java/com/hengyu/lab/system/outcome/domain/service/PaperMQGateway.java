package com.hengyu.lab.system.outcome.domain.service;

import com.hengyu.lab.system.outcome.domain.vo.PaperMetaTask;

public interface PaperMQGateway {
  public void publishTask(PaperMetaTask message);
}
