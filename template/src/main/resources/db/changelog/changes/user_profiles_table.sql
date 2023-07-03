-- liquibase formatted sql

-- changeset Dovidas Zablockis:1
-- comment: create users table
CREATE TABLE `user_profiles` (
    `id` UUID NOT NULL DEFAULT random_uuid(),
    `user_id` UUID NOT NULL,
    `first_name` VARCHAR(35) NOT NULL,
    `last_name` VARCHAR(35) NOT NULL,
    `birthday` DATE NOT NULL,
    `ssn` VARCHAR(20) NOT NULL,

    CONSTRAINT `PK_USER_PROFILES`
    PRIMARY KEY (`id`),

    CONSTRAINT `FK_USER_PROFILES`
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT `UQ_USER_PROFILES`
    UNIQUE (`ssn`)
);
-- rollback DROP TABLE `user_profiles`;