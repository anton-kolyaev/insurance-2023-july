
ALTER TABLE IF EXISTS `java_internship`.`users`
ALTER COLUMN `user_name` RENAME TO `username`;

ALTER TABLE IF EXISTS `java_internship`.`users`
ALTER COLUMN `snn` RENAME TO `ssn`;