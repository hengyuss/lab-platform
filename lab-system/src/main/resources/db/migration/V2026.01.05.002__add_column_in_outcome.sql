ALTER TABLE sys_outcome_paper
    ADD COLUMN `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除(0未删,1已删)';