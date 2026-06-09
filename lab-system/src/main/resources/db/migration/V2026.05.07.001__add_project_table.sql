drop table if exists sys_outcome_project;
CREATE TABLE `sys_outcome_project`
(
    `outcome_id`          BIGINT NOT NULL COMMENT '成果ID，主键（与主表 outcome 的 ID 保持一致）',
    `fund`                JSON        DEFAULT NULL COMMENT '基金列表（JSON 格式存储）',
    `responsible_persons` JSON        DEFAULT NULL COMMENT '负责人列表（JSON 格式存储）',
    `project_type`        VARCHAR(50) DEFAULT NULL COMMENT '项目类型',
    `start_time`          DATETIME    DEFAULT NULL COMMENT '开始时间',
    `end_time`            DATETIME    DEFAULT NULL COMMENT '结束时间',
    `project_indicator`   VARCHAR(50) DEFAULT NULL COMMENT '项目指标',
    PRIMARY KEY (`outcome_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='项目成果扩展表';
