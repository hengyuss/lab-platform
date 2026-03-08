package com.hengyu.lab.system.outcome.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hengyu.lab.common.exception.BizException;
import com.hengyu.lab.system.outcome.application.dto.command.DblpTeacherDTO;
import com.hengyu.lab.system.outcome.infrastructure.mapper.TeacherDblpPidMapper;
import com.hengyu.lab.system.outcome.infrastructure.po.TeacherDblpPidPO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TeacherDblpPidService {

  private final TeacherDblpPidMapper teacherDblpPidMapper;

  public void addTeacher(DblpTeacherDTO dto) {
    String teacherName = dto.getTeacherName();
    String pid = dto.getPid();
    if (!StringUtils.hasText(teacherName) || !StringUtils.hasText(pid)) {
      throw new IllegalArgumentException("老师姓名和pid不能为空");
    }

    boolean exists = teacherDblpPidMapper.exists(
        new LambdaQueryWrapper<TeacherDblpPidPO>().eq(TeacherDblpPidPO::getPid, pid)
    );
    if (exists) {
      throw new BizException("该老师已存在，请勿重复添加");
    }

    TeacherDblpPidPO po = new TeacherDblpPidPO();
    po.setTeacherName(teacherName);
    po.setPid(pid);
    teacherDblpPidMapper.insert(po);
  }

  public void removeTeacher(String pid) {
    if (!StringUtils.hasText(pid)) {
      throw new IllegalArgumentException("老师pid 不能为空");
    }
    LambdaQueryWrapper<TeacherDblpPidPO> wrapper = new LambdaQueryWrapper<TeacherDblpPidPO>().eq(
        TeacherDblpPidPO::getPid, pid);
    teacherDblpPidMapper.delete(wrapper);
  }

  public IPage<TeacherDblpPidPO> listCurrentTeachers(int pageNo, int pageSize) {
    Page<TeacherDblpPidPO> page = new Page<>(pageNo, pageSize);
    Page<TeacherDblpPidPO> teacherDblpPidPOPage = teacherDblpPidMapper.selectPage(page, null);
    return teacherDblpPidPOPage;
  }

  public List<TeacherDblpPidPO> listCurrentTeachers() {
    return teacherDblpPidMapper.selectList(null);
  }

}
