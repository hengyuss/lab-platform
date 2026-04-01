package com.hengyu.lab.system.outcome.domain.service;

import com.hengyu.lab.system.outcome.domain.valobj.PaperMetaTask;

public interface PaperMQGateway {
  public void publishTask(PaperMetaTask message);
}
