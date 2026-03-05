DROP TABLE if exists sys_teacher_dblp_pid;
CREATE TABLE sys_teacher_dblp_pid
(
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `teacher_name` VARCHAR(100) NOT NULL COMMENT '作者英文名',
    `pid` VARCHAR(255) NOT NULL COMMENT 'dblp中作者对应的唯一标识',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`pid`)
)