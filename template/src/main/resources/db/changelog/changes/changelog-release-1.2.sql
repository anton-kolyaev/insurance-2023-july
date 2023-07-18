-- liquibase formatted sql

-- changeset dovidas.zablockis:5
-- comment: create user auth table
CREATE TABLE `user_auth` (
    `id` UUID NOT NULL,
    `username` VARCHAR (35) NOT NULL,
    `password` VARCHAR (100) NOT NULL,
    `role` ENUM (
        'VIEWER',
        'MODERATOR',
        'ADMIN'
    ),
    `status` ENUM (
        'ACTIVE',
        'DEACTIVATED',
        'DELETED'
    ),

    CONSTRAINT `PK_USER_AUTH`
    PRIMARY KEY (`id`),

    CONSTRAINT `UQ_USER_AUTH`
    UNIQUE (`username`)
);
-- rollback DROP TABLE `user_auth`;

-- changeset dovidas.zablockis:6
-- comment: add auth id inside users table
ALTER TABLE `users` ADD COLUMN `auth_id` UUID AFTER `id`;

ALTER TABLE `users` ADD CONSTRAINT `FK_USERS`
FOREIGN KEY (`auth_id`) REFERENCES `user_auth` (`id`)
ON UPDATE CASCADE ON DELETE SET NULL;
-- rollback ALTER TABLE `users` DROP CONSTRAINT `FK_USERS`;
-- rollback ALTER TABLE `users` DROP COLUMN `auth_id`;