ALTER TABLE sys_paper_outcome
ADD COLUMN `partition` int COMMENT '期刊分区',
ADD COLUMN `factor` decimal(8, 3) COMMENT '影响因子';