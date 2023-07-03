-- liquibase formatted sql
-- changeset dzmitry.papkou:0.0.0
CREATE SCHEMA `java_internship`;

CREATE TABLE `java_internship`.`users`(
`id` UUID NOT NULL,
`first_name` VARCHAR(35) NOT NULL,
`last_name` VARCHAR(35) NOT NULL,
`birthday` DATE NOT NULL,
`user_name` VARCHAR(35) NOT NULL,
`email` VARCHAR(100) NOT NULL,
`snn` VARCHAR(20) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE (`user_name`),
UNIQUE(`snn`)
);
-- rollback DROP TABLE `java_internship`.`users`;
-- rollback DROP SCHEMA `java-internship`;

-- changeset dzmitry.papkou:0.0.1
ALTER TABLE IF EXISTS `java_internship`.`users`
ALTER COLUMN `user_name` RENAME TO `username`;

ALTER TABLE IF EXISTS `java_internship`.`users`
ALTER COLUMN `snn` RENAME TO `ssn`;

-- rollback ALTER TABLE IF EXISTS `java_internship`.`users`
-- rollback ALTER COLUMN `username` RENAME TO `user_name`;

-- rollback ALTER TABLE IF EXISTS `java_internship`.`users`
-- rollback ALTER COLUMN `ssn` RENAME TO `snn`;

-- changeset dzmitry.papkou:0.0.2
ALTER TABLE IF EXISTS `java_internship`.`users`
ADD COLUMN `deletion_status` BOOLEAN NOT NULL DEFAULT FALSE;

-- rollback ALTER TABLE IF EXISTS `java_internship`.`users`
-- rollback DROP COLUMN `deletion_status`;