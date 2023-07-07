-- liquibase formatted sql

-- changeset dovias:0.0.1
CREATE TABLE `company_package_plans` (
    `id` UUID NOT NULL,
    `package_id` UUID NOT NULL,
    `name` VARCHAR (100) NOT NULL,
    `type` ENUM ('DENTAL', 'MEDICAL') NOT NULL,
    `contributions` DECIMAL (10, 2) NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`package_id`)
    -- comment: if you're getting syntax error on an IDE on `company_plan_packages` table name,
    -- comment: ignore it, its due to the fact that the definition is in another file.
    REFERENCES `company_plan_packages` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)
-- rollback DROP TABLE `package_plans`;