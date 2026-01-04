DROP TABLE IF EXISTS sys_outcome;
CREATE TABLE sys_outcome
(
    `id`          BIGINT(20)      NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title`       VARCHAR(512) NOT NULL COMMENT '成果标题(论文名/专利名)',

    -- 枚举字段建议存字符串，可读性好；也可以存 TINYINT 节省空间
    `type`        VARCHAR(32)  NOT NULL COMMENT '成果类型',
    `status`      VARCHAR(32)  NOT NULL COMMENT '状态',

    -- 所有的表建议都加上这4个审计字段 (DDD中的基础字段)
    `create_by`   VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    `update_by`    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',

    PRIMARY KEY (`id`),
    KEY           `idx_type` (`type`),          -- 为类型加索引，方便分类查询
    KEY           `idx_create_by` (`create_by`) -- 方便查询“我提交的成果”
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研成果主表';


DROP TABLE IF EXISTS sys_outcome_author;
CREATE TABLE sys_outcome_author
(
    `id`               BIGINT(20)      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `outcome_id`       BIGINT(20)      NOT NULL                COMMENT '关联成果ID (sys_outcome.id)',

    `user_id`          BIGINT(20)      DEFAULT NULL            COMMENT '系统用户ID (如果是校内人员)',
    `author_name`      VARCHAR(64) NOT NULL COMMENT '作者姓名 (如果是校外人员，存文本)',

    `sort`             INT(11)         NOT NULL DEFAULT 1      COMMENT '作者排名(第几作者)',
    `is_corresponding` TINYINT(1)   NOT NULL DEFAULT 0      COMMENT '是否通讯作者 (0否 1是)',

    PRIMARY KEY (`id`),
    KEY                `idx_outcome_id` (`outcome_id`) -- 🔥 核心索引：查询某个成果的所有作者
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成果作者关联表';