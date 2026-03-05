package com.hengyu.lab.system.outcome.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hengyu.lab.common.api.R;
import com.hengyu.lab.system.outcome.application.service.TeacherDblpPidService;
import com.hengyu.lab.system.outcome.infrastructure.po.TeacherDblpPidPO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dblp/teacher")
@Tag(name = "需要爬取的老师的pid操作")
@RequiredArgsConstructor
public class TeacherDblpPidController {

  private final TeacherDblpPidService teacherDblpPidService;

  @PostMapping("/add")
  public R addTeacher(@RequestParam("teacherName") String teacherName,
      @RequestParam("pid") String pid) {
    teacherDblpPidService.addTeacher(teacherName, pid);
    return R.ok();
  }

  @DeleteMapping("/remove")
  public R removeTeacher(@RequestParam("pid") String pid) {
    teacherDblpPidService.removeTeacher(pid);
    return R.ok();
  }

  @GetMapping("/list")
  public R<IPage<TeacherDblpPidPO>> listTeachers(
      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    IPage<TeacherDblpPidPO> data = teacherDblpPidService.listCurrentTeachers(
        pageNo, pageSize);
    return R.ok(data);
  }

}
