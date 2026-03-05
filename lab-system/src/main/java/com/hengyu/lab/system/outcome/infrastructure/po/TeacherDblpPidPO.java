package com.hengyu.lab.system.outcome.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_teacher_dblp_pid")
@Data
public class TeacherDblpPidPO {
  @TableId(type = IdType.AUTO)
  private Integer id;
  private String teacherName;
  private String pid;
}
