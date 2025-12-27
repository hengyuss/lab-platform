drop table if exists sys_role;
create table sys_role (
    role_id              bigint(20)      not null auto_increment    comment '角色ID',
    role_name            varchar(30)     not null                   comment '角色名称',
    role_key             varchar(100)    not null                   comment '角色权限字符串',
    role_sort            int(4)          not null                   comment '显示顺序',
    data_scope           char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    status               char(1)         not null                   comment '角色状态（0正常 1停用）',
    deleted             char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
    menu_check_strictly  tinyint(1)      default 1                  comment '菜单树选择项是否关联显示',
    create_time          datetime                                   comment '创建时间',
    update_time          datetime                                   comment '更新时间',
    primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

insert into sys_role values('1', '超级管理员',  'admin',  1, 1, 0, 0, 1, now(), null);
insert into sys_role values('2', '普通角色',    'common', 2, 2, 0, 0, 1, now(), null);

-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
      menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
      menu_name         varchar(50)     not null                   comment '菜单名称',
      parent_id         bigint(20)      default 0                  comment '父菜单ID',
      order_num         int(4)          default 0                  comment '显示顺序',
      path              varchar(200)    default ''                 comment '路由地址',
      component         varchar(255)    default null               comment '组件路径',
      query             varchar(255)    default null               comment '路由参数',
      route_name        varchar(50)     default ''                 comment '路由名称',
      is_frame          int(1)          default 1                  comment '是否为外链（0是 1否）',
      is_cache          int(1)          default 0                  comment '是否缓存（0缓存 1不缓存）',
      menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
      visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
      status            char(1)         default 0                  comment '菜单状态（0正常 1停用）',
      perms             varchar(100)    default null               comment '权限标识',
      icon              varchar(100)    default '#'                comment '菜单图标',
      create_by         varchar(64)     default ''                 comment '创建者',
      create_time       datetime                                   comment '创建时间',
      update_by         varchar(64)     default ''                 comment '更新者',
      update_time       datetime                                   comment '更新时间',
      remark            varchar(500)    default ''                 comment '备注',
      primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
       user_id   bigint(20) not null comment '用户ID',
       role_id   bigint(20) not null comment '角色ID',
       primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
       role_id   bigint(20) not null comment '角色ID',
       menu_id   bigint(20) not null comment '菜单ID',
       primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';
