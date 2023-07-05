-- liquibase formatted sql

-- changeset Dzmitry Papkou:1
-- comment: add default schema
CREATE SCHEMA `insurance_manager`;
SET SCHEMA `insurance_manager`;
-- rollback DROP SCHEMA `insurance_manager`;
-- rollback SET SCHEMA PUBLIC;

-- changeset Dzmitry Papkou:2
-- comment: add users table
CREATE TABLE `users` (
    `id` UUID NOT NULL,
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

-- changeset Dovidas Zablockis:3
-- comment: create user auth table
CREATE TABLE `user_auth` (
    `id` UUID NOT NULL,
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