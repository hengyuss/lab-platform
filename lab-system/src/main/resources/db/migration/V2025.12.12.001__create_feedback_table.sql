CREATE TABLE  if not exists t_feedback
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    title       VARCHAR(255) NOT NULL COMMENT '标题',
    content     TEXT COMMENT '内容',
    status      INT      DEFAULT 0 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     INT      DEFAULT 0,
    PRIMARY KEY (id)
);