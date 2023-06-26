-- liquibase formatted sql

-- changeset dovias:1
CREATE TABLE `insurance_manager_v1`.`companies` (
    `id` UUID NOT NULL,
    `name` VARCHAR (35) NOT NULL,
    `country_code` VARCHAR (2) NOT NULL,
    `email` VARCHAR (100) NOT NULL,
    `website` VARCHAR (100),

    PRIMARY KEY (`id`)
);
-- rollback DROP TABLE `companies`;