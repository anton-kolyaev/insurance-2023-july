-- liquibase formatted sql

-- changeset dovias:1
CREATE TABLE `insurance_manager`.`company_phones` (
    `company_id` UUID NOT NULL,
    `code` VARCHAR(10) NOT NULL,
    `number` VARCHAR(20) NOT NULL,

    FOREIGN KEY (`company_id`)
    -- comment: if you're getting syntax error on an IDE on `companies` table name,
    -- comment: ignore it, its due to the fact that the definition is in another file.
    REFERENCES `insurance_manager`.`companies` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)
-- rollback DROP TABLE `company_phones`;