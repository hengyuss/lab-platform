package com.hengyu.lab.framework.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class DomainUtil {

  private DomainUtil() {}

  public static <T> void setIdToEntity(T entity, Long id) {
    setIdToEntity(entity, id, "id");
  }

  public static <T> void setIdToEntity(T entity, Long id, String IdName) {
    if (entity == null) {
      return;
    }
    Field idField = ReflectionUtils.findField(entity.getClass(), IdName);
    if (idField == null) {
      throw new RuntimeException(
          "在类 " + entity.getClass().getName() + " 中找不到 'id' 字段，无法回填 ID");
    }
    // 2. 暴力破解 private 权限
    ReflectionUtils.makeAccessible(idField);
    // 3. 设置值
    ReflectionUtils.setField(idField, entity, id);
  }
}

