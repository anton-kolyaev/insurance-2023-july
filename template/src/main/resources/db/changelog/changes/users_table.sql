-- liquibase formatted sql

-- changeset Dovidas Zablockis:1
-- comment: create user profiles table
CREATE TABLE `users` (
    `id` UUID NOT NULL DEFAULT random_uuid(),
    `username` VARCHAR_IGNORECASE (35) NOT NULL,
    `password` VARCHAR (100) NOT NULL,
    `email` VARCHAR_IGNORECASE (100) NOT NULL,
    `role` ENUM (
        'VIEWER',
        'MODERATOR',
        'ADMIN'
    ) NOT NULL DEFAULT 'VIEWER',

    CONSTRAINT `PK_USERS`
    PRIMARY KEY (`id`),

    CONSTRAINT `UQ_USERS`
    UNIQUE (`username`, `email`)
);
-- rollback DROP TABLE `users`;
