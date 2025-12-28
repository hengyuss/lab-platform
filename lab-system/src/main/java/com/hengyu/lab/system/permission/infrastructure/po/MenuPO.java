package com.hengyu.lab.system.permission.infrastructure.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
