-- liquibase formatted sql

-- changeset dzmitry.papkou:1
-- comment: add default schema
CREATE SCHEMA `insurance_manager`;
SET SCHEMA `insurance_manager`;
-- rollback DROP SCHEMA `insurance_manager`;
-- rollback SET SCHEMA PUBLIC;

-- changeset dzmitry.papkou:2
-- comment: add users table
CREATE TABLE `users` (
    `id` UUID NOT NULL DEFAULT random_uuid(),
    `first_name` VARCHAR (35) NOT NULL,
    `last_name` VARCHAR (35) NOT NULL,
    `birthday` DATE NOT NULL,
    `email` VARCHAR (100) NOT NULL,
    `ssn` VARCHAR (20) NOT NULL,
    
    CONSTRAINT `PK_USERS`
    PRIMARY KEY (`id`),

    CONSTRAINT `UQ_USERS`
    UNIQUE (`ssn`)
);
-- rollback DROP TABLE `users`;

-- changeset dzmitry.papkou:3
-- comment: add deletion status to users table
ALTER TABLE IF EXISTS `users`
ADD COLUMN `deletion_status` BOOLEAN NOT NULL DEFAULT FALSE;

-- rollback ALTER TABLE IF EXISTS `users`
-- rollback DROP COLUMN `deletion_status`;