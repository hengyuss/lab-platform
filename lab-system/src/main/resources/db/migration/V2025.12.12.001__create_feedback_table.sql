CREATE TABLE `t_feedback`
(
    -- 1. 主键 ID
    -- 对应 @TableId(type = IdType.ASSIGN_ID)
    -- 必须是 BIGINT，且千万不要加 AUTO_INCREMENT，因为是 Java 层生成的 ID
    `id`          BIGINT       NOT NULL COMMENT '主键ID',

    -- 2. 业务字段
    -- 对应 String title
    `title`       VARCHAR(255) NOT NULL COMMENT '反馈标题',
    -- 对应 String content
    -- 内容可能较长，建议用 TEXT 或 LONGTEXT
    `content`     TEXT COMMENT '反馈内容',

    -- 对应 FeedbackStatus status
    -- 之前约定了枚举存 0, 1, 2，所以用 TINYINT
    `status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态(0:待处理, 1:已采纳, 2:已驳回)',

    -- 3. BasePO 字段 (自动填充 & 逻辑删除)
    -- 驼峰 createTime -> 下划线 create_time
    `create_time` DATETIME COMMENT '创建时间',

    -- 驼峰 updateTime -> 下划线 update_time
    `update_time` DATETIME COMMENT '更新时间',

    -- 对应 @TableLogic Integer deleted
    -- 逻辑删除通常用 TINYINT，0表示未删，1表示已删
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除(0:未删, 1:已删)',

    -- 4. 约束
    PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='反馈表';