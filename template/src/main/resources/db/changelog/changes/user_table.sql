-- liquibase formatted sql

-- changeset dovias:1
CREATE TABLE `java_internship`.`users` (
    `id` UUID NOT NULL,
    `first_name` VARCHAR(35) NOT NULL,
    `last_name` VARCHAR(35) NOT NULL,
    `birthday` DATE NOT NULL,
    `user_name` VARCHAR(35) NOT NULL,
    `email` VARCHAR(100) NOT NULL,
    `snn` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`user_name`),
    UNIQUE (`snn`)
);
-- rollback DROP TABLE `users`;
