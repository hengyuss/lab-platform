package com.hengyu.lab.system.infrastructure.persistence.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hengyu.lab.common.persistence.BasePO;
import com.hengyu.lab.system.domain.feedback.constant.FeedbackStatus;
import lombok.Data;

@TableName("t_feedback")
@Data
public class FeedbackPO extends BasePO {

  @TableId(type = IdType.ASSIGN_ID)
  private Long id;

  private String title;

  private String content;

  private FeedbackStatus status;
}
