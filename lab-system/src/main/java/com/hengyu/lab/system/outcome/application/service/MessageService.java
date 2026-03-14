package com.hengyu.lab.system.outcome.application.service;

import com.hengyu.lab.system.outcome.application.dto.PaperMessage;
import com.hengyu.lab.system.outcome.domain.service.PaperMQGateway;
import com.hengyu.lab.system.outcome.domain.vo.PaperMetaTask;
import com.hengyu.lab.system.outcome.infrastructure.po.TeacherDblpPidPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

  private final PaperMQGateway paperMQGateway;
  private final TeacherDblpPidService teacherDblpPidService;

  public void sendPaperMessage(PaperMessage message) {
    boolean isAllTeachers = message.getTeacherName().equals(PaperMetaTask.DEFAULT_TEACHER_NAME);
    if (!isAllTeachers) {
      PaperMetaTask task = new PaperMetaTask();
      BeanUtils.copyProperties(message, task);
      paperMQGateway.publishTask(task);
      return;
    }

    List<TeacherDblpPidPO> teacherDblpPidPOS = teacherDblpPidService.listCurrentTeachers();
    if (CollectionUtils.isEmpty(teacherDblpPidPOS)) {
      return;
    }

    teacherDblpPidPOS.forEach(teacherDblpPidPO -> {
      PaperMetaTask task = new PaperMetaTask();
      task.setTeacherName(teacherDblpPidPO.getTeacherName());
      task.setTeacherPid(teacherDblpPidPO.getPid());
      paperMQGateway.publishTask(task);
    });
  }

}
