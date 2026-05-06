ALTER TABLE sys_outcome_paper
ADD COLUMN `journal_partition` int COMMENT '期刊分区',
ADD COLUMN `factor` decimal(8, 3) COMMENT '影响因子';