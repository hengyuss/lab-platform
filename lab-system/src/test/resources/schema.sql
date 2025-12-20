CREATE TABLE if not exists t_feedback
(
    id
    BIGINT
    NOT
    NULL
    COMMENT
    '主键',
    title
    VARCHAR
(
    255
) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    status INT DEFAULT 0 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    PRIMARY KEY
(
    id
)
    );


CREATE TABLE IF NOT EXISTS sys_user
(
    id
    BIGINT
    NOT
    NULL
    COMMENT
    '主键',
    username
    VARCHAR
(
    128
) NOT NULL COMMENT '用户名',
    password VARCHAR
(
    255
) NOT NULL COMMENT '密码',
    real_name VARCHAR
(
    128
) NOT NULL COMMENT '真实姓名',
    email VARCHAR
(
    128
) NOT NULL COMMENT '邮箱',
    mobile VARCHAR
(
    128
) NOT NULL COMMENT '手机号',
    identity_type INT DEFAULT 1 COMMENT '业务身份',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted INT DEFAULT 0,
    CONSTRAINT uk_username UNIQUE
(
    username
),
    CONSTRAINT uk_mobile UNIQUE
(
    mobile
),
    CONSTRAINT uk_email UNIQUE
(
    email
),
    PRIMARY KEY
(
    id
)
    );
