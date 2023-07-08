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

-- changeset dovidas.zablockis:3
-- comment: create user auth table
CREATE TABLE `user_auth` (
    `id` UUID NOT NULL DEFAULT random_uuid(),
    `username` VARCHAR_IGNORECASE (35) NOT NULL,
    `password` VARCHAR (100) NOT NULL,
    `role` ENUM (
        'VIEWER',
        'MODERATOR',
        'ADMIN'
    ) NOT NULL DEFAULT 'VIEWER',
    `status` ENUM (
        'ACTIVE',
        'DEACTIVATED',
        'DELETED'
    ) NOT NULL DEFAULT 'ACTIVE',

    CONSTRAINT `PK_USER_AUTH`
    PRIMARY KEY (`id`),

    CONSTRAINT `UQ_USER_AUTH`
    UNIQUE (`username`)
);
-- rollback DROP TABLE `user_auth`;

-- changeset dovidas.zablockis:4
-- comment: add auth id inside userEntities table
ALTER TABLE `users` ADD COLUMN `auth_id` UUID AFTER `id`;
ALTER TABLE `users` ADD CONSTRAINT `FK_USERS`
FOREIGN KEY (`auth_id`) REFERENCES `user_auth` (`id`);
-- rollback ALTER TABLE `users` DROP CONSTRAINT `FK_USERS`;
-- rollback ALTER TABLE `users` DROP COLUMN `auth_id`;

-- changeset dovidas.zablockis:5
-- comment: add plan packages table
CREATE TABLE `plan_packages` (
    `id` UUID NOT NULL DEFAULT random_uuid(),
    `name` VARCHAR (35) NOT NULL,
    `payroll` ENUM (
        'WEEKLY',
        'MONTHLY'
    ) NOT NULL,
    `starts` DATE NOT NULL,
    `expires` DATE NOT NULL,

    CONSTRAINT `PK_PLAN_PACKAGES`
    PRIMARY KEY (`id`)
);
-- rollback DROP TABLE `plan_packages`;

-- changeset dovidas.zablockis:6
-- comment: create plans table
CREATE TABLE `plans` (
    `id` UUID NOT NULL DEFAULT random_uuid(),
    `package_id` UUID NOT NULL,
    `name` VARCHAR (35) NOT NULL,
    `type` ENUM (
        'MEDICAL',
        'DENTAL'
    ) NOT NULL,
    `contributions` DECIMAL (20, 2) NOT NULL,

    CONSTRAINT `PK_PLANS`
    PRIMARY KEY (`id`),

    CONSTRAINT `FK_PLANS`
    FOREIGN KEY (`package_id`) REFERENCES `plan_packages` (`id`)
    ON UPDATE CASCADE ON DELETE CASCADE
);
-- rollback DROP TABLE `plans`;