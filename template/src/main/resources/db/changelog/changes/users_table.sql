-- liquibase formatted sql

-- changeset dovias:0.0.1
CREATE TABLE `users` (
    `id` UUID NOT NULL,
    `username` VARCHAR (100) NOT NULL,
    `password` VARCHAR (100) NOT NULL,

    PRIMARY KEY (`id`)
);
-- rollback DROP TABLE `users`;