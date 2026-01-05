package com.hengyu.lab.system.permission.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {

  private static final long serialVersionUID = 1L;

  private Long roleId;
  private String roleName;
  //
  private String roleKey;
  //(0 正常使用， 1 停用）
  private String status;
  private Integer roleSort;

  private List<Long> menuIds;


  public void assignMenuIds(List<Long> menuIds) {
    this.menuIds = menuIds;
  }
}
