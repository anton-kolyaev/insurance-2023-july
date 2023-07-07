-- liquibase formatted sql

-- changeset dovias:0.0.1
CREATE TABLE `user_profile_addresses` (
    `user_id` UUID NOT NULL,
    `country` VARCHAR (100) NOT NULL,
    `street` VARCHAR (100) NOT NULL,
    `city` VARCHAR (100) NOT NULL,
    `building` VARCHAR (50) NOT NULL,
    `state` VARCHAR (50) NOT NULL,
    `room` VARCHAR (20),

    FOREIGN KEY (`user_id`)
    -- comment: if you're getting syntax error on an IDE on `users` table name,
    -- comment: ignore it, its due to the fact that the definition is in another file.
    REFERENCES `users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)
-- rollback DROP TABLE `user_profile_addresses`;