package com.hengyu.lab.common.utils;

import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;

public class DomainUtil {

  public static <T> void setIdToEntity(T entity, Long id) {
    if (entity == null) {
      return;
    }
    Field idField = ReflectionUtils.findField(entity.getClass(), "id");
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

