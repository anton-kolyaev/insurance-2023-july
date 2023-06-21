-- liquibase formatted sql

-- changeset dovias:1
CREATE TABLE `insurance_manager_v1`.`users` (
    `id` UUID NOT NULL,
    `username` VARCHAR(100) NOT NULL,
    `password` VARCHAR(100) NOT NULL,

    PRIMARY KEY (`id`)
);
-- rollback DROP TABLE `users`;
