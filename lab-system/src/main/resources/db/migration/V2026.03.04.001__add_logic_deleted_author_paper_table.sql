ALTER TABLE sys_outcome_author
ADD COLUMN `deleted` INT DEFAULT 0;

ALTER TABLE sys_outcome_paper
ADD COLUMN `deleted` INT DEFAULT 0;