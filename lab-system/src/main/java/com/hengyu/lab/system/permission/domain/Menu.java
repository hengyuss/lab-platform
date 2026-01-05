package com.hengyu.lab.system.permission.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu {

  private Long menuId;

  private String menuName;

  private String perms;


}
