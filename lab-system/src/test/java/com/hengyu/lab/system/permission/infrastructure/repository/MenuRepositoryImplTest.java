package com.hengyu.lab.system.permission.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.hengyu.lab.system.permission.domain.Menu;
import com.hengyu.lab.system.permission.domain.repository.MenuRepository;
import com.hengyu.lab.system.permission.infrastructure.convert.MenuConverter;
import com.hengyu.lab.system.permission.infrastructure.mapper.MenuMapper;
import com.hengyu.lab.system.permission.infrastructure.po.MenuPO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MenuRepositoryImplTest {

  @Autowired
  private MenuRepository menuRepository;

  @Autowired
  @SpyBean
  private MenuMapper menuMapper;

  @Autowired
  private MenuConverter menuConverter;

  @Test
  void save_menu_not_exist() throws Exception {
    Menu menu = Menu.builder()
        .perms("temp:perm")
        .menuName("测试菜单")
        .build();

    menuRepository.save(menu);

    Optional<Menu> optionalMenu = menuRepository.findById(menu.getMenuId());
    assertTrue(optionalMenu.isPresent());
    assertEquals("temp:perm", optionalMenu.get().getPerms());
    assertEquals("测试菜单", optionalMenu.get().getMenuName());
  }

  @Test
  void save_menu_exist() throws Exception {
    Menu menu1 = Menu.builder()
        .perms("temp:perm")
        .menuName("测试菜单")
        .build();

    menuRepository.save(menu1);
    Optional<Menu> optionalMenu1 = menuRepository.findById(menu1.getMenuId());
    assertTrue(optionalMenu1.isPresent());
    assertEquals("temp:perm", optionalMenu1.get().getPerms());
    assertEquals("测试菜单", optionalMenu1.get().getMenuName());

    Menu menu2 = Menu.builder()
        .menuId(menu1.getMenuId())
        .perms("modify:perm")
        .menuName("更改菜单")
        .build();
    menuRepository.save(menu2);
    Optional<Menu> optionalMenu2 = menuRepository.findById(menu2.getMenuId());
    assertTrue(optionalMenu2.isPresent());
    assertEquals("modify:perm", optionalMenu2.get().getPerms());
    assertEquals("更改菜单", optionalMenu2.get().getMenuName());
    Mockito.verify(menuMapper).updateById(Mockito.any(MenuPO.class));
  }

  @Test
  void list_menu(){
    MenuPO menuPO1 = MenuPO.builder()
        // ID通常插入时为空，查询测试时才填
        // .menuId(1L)
        .menuName("系统管理")
        .parentId(0L)          // 一级目录
        .orderNum(1)
        .path("system")        // 路由地址
        .component(null)       // 目录通常没有组件
        .isFrame("1")            // 不是外链
        .menuType("M")         // M=Directory
        .visible("0")          // 显示
        .status("0")           // 正常
        .perms(null)           // 目录通常没有权限标识
        .icon("system")
        .build();

    menuMapper.insert(menuPO1);

    Menu menu = Menu.builder().build();
    List<Menu> menuList = menuRepository.selectMenuList(menu);
    assertEquals(7, menuList.size());
    assertEquals("系统管理",  menuList.get(0).getMenuName());
  }

  @Test
  void get_menu_perms_by_role_ids(){
    Set<String> permsByRoleIds = menuRepository.getPermsByRoleIds(List.of(1L, 2L));
    Assertions.assertEquals(Set.of("system:user:list", "system:role:list"),  permsByRoleIds);
  }



}