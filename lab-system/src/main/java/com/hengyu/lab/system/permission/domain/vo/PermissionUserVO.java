package com.hengyu.lab.system.permission.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionUserVO {
  private Long userId;
  private String roleName;
  private List<Long> roleIds;

}
