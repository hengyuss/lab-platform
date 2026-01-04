DROP TABLE IF EXISTS sys_outcome_paper;
CREATE TABLE sys_outcome_paper
(
    -- 这里不使用 auto_increment，因为它必须和主表 sys_outcome 的 id 保持一致
    `outcome_id`   BIGINT(20)      NOT NULL                COMMENT '成果ID (关联 sys_outcome.id)',

    -- 2. 你的业务字段
    `journal_name` VARCHAR(255) DEFAULT NULL COMMENT '期刊名称',
    `issn`         VARCHAR(32)  DEFAULT NULL COMMENT 'ISSN号 (例如: 1234-567X)',
    `publish_time` DATETIME     DEFAULT NULL COMMENT '发表时间',

    -- 3. BasePO 继承来的通用字段 (看你的需求决定是否保留)
    -- 扩展表通常不需要独立的 create_time/update_time，因为它是随主表一起创建的
    -- 但如果你希望代码里的 BasePO 能自动填充，可以在这里加上
    `create_by`    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    `create_time`  DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    `update_time`  DATETIME     DEFAULT NULL COMMENT '更新时间',
    `remark`       VARCHAR(500) DEFAULT NULL COMMENT '备注',

    -- 4. 索引约束
    PRIMARY KEY (`outcome_id`) -- outcome_id 既是主键，也是逻辑外键
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研成果-论文详情表';