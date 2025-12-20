package com.hengyu.lab.common.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import java.time.LocalDateTime;
import lombok.Data;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class BasePoMetaObjectHandlerTest {

  private final MetaObjectHandler metaObjectHandler = new BasePoMetaObjectHandler();

  // 1. 【关键修改】必须加上注解，否则 strictInsertFill 会认为该字段不需要填充
  @Data
  @TableName("test_po") // 假装它是个表
  static class TestPO {

    @TableField(fill = FieldFill.INSERT) // 必须有
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 必须有
    private LocalDateTime updateTime;

    private String otherField;
  }

  // 2. 【关键修改】手动初始化 TableInfo
  @BeforeEach
  void initTableInfo() {
    // 这一步模拟了 MyBatis Plus 启动时的实体扫描
    // 如果没有这一步，strictInsertFill 找不到 TableInfo 就会报 NPE
    TableInfoHelper.initTableInfo(
        new MapperBuilderAssistant(new MybatisConfiguration(), ""),
        TestPO.class
    );
  }

  @Test
  void insertFill_ShouldFillCreateTimeAndUpdateTime() {
    TestPO po = new TestPO();
    MetaObject metaObject = SystemMetaObject.forObject(po);

    metaObjectHandler.insertFill(metaObject);

    assertNotNull(po.getCreateTime(), "insert 操作应该填充 createTime");
    assertNotNull(po.getUpdateTime(), "insert 操作应该填充 updateTime");
  }

  @Test
  void updateFill_ShouldOnlyFillUpdateTime() {
    TestPO po = new TestPO();
    LocalDateTime oldCreateTime = LocalDateTime.of(2020, 1, 1, 0, 0);
    po.setCreateTime(oldCreateTime);

    MetaObject metaObject = SystemMetaObject.forObject(po);

    metaObjectHandler.updateFill(metaObject);

    assertNotNull(po.getUpdateTime(), "update 操作应该填充 updateTime");
    // 验证 createTime 没有被篡改
    assertEquals(oldCreateTime, po.getCreateTime());
  }
}