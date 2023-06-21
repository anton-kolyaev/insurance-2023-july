-- liquibase formatted sql

-- changeset dovias:1
CREATE TABLE `insurance_manager`.`company_plan_packages` (
    `id` UUID NOT NULL,
    `company_id` UUID NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `payroll_frequency` ENUM ('MONTHLY', 'WEEKLY') NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`company_id`)
    -- comment: if you're getting syntax error on an IDE on `companies` table name,
    -- comment: ignore it, its due to the fact that the definition is in another file.
    REFERENCES `insurance_manager`.`companies` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)
-- rollback DROP TABLE `company_plan_packages`;