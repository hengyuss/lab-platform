package com.hengyu.lab.system.permission.infrastructure.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@TableName("sys_menu")
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPO {

  private static final long serialVersionUID = 1L;
  @TableId(type = IdType.AUTO)
  private Long menuId;

  private String menuName;

  private Long parentId;

  private Integer orderNum;

  private String path;

  private String component;

  private String query;

  private String routeName;

  private String isFrame;

  private String menuType;

  private String visible;

  private String status;

  private String perms;

  private String icon;

  private String remark;

  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;


}
