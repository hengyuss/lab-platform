package com.hengyu.lab.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

class DomainUtilTest {

  static class TestEntity {
    Long id;
    public void setId(Long id) {
      this.id = id;
    }
    public Long getId() {
      return id;
    }
  }


  static class TestEntityNoId {

  }

  @Test
  void testDomainUtil() {
    TestEntity entity = new TestEntity();
    DomainUtil.setIdToEntity(entity, 1L);
    Assertions.assertEquals(1L, entity.getId());
  }

  @Test
  void testDomainUtilNoId() {
    TestEntityNoId entity = new TestEntityNoId();
    Assertions.assertThrows(RuntimeException.class, () -> {
      DomainUtil.setIdToEntity(entity, 1L);
    });
  }

  @Test
  void testDomainUtil_entity_is_null() {
    DomainUtil.setIdToEntity(null, 1L);
    try(MockedStatic<ReflectionUtils> reflectionUtilsStatic = Mockito.mockStatic(ReflectionUtils.class)) {
      reflectionUtilsStatic.verifyNoInteractions();
    }
  }

}