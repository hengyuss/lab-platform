ALTER TABLE sys_outcome_paper
    ADD COLUMN `dblp_key` VARCHAR(255) COMMENT 'DBLP唯一标识',
    ADD COLUMN `year` INT COMMENT '发表年份',
    ADD COLUMN `ee` VARCHAR(512) COMMENT '电子版链接',
    ADD COLUMN `authors` TEXT COMMENT '作者列表',
    ADD COLUMN `paper_type` VARCHAR(50) COMMENT '论文类型';