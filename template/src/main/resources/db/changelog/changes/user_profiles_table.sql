-- liquibase formatted sql

-- changeset dovias:0.0.1
CREATE TABLE `user_profiles` (
    `user_id` UUID NOT NULL,
    `first_name` VARCHAR (35) NOT NULL,
    `last_name` VARCHAR (35) NOT NULL,
    `birthday` DATE NOT NULL,
    `email` VARCHAR (100) NOT NULL,
    `snn` VARCHAR (20) NOT NULL UNIQUE,

    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
);
-- rollback DROP TABLE `user_profiles`;
