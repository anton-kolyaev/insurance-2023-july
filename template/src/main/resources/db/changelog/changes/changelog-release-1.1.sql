-- liquibase formatted sql

-- changeset dzmitry.papkou:5
-- comment: add deletion status to companies table
ALTER TABLE IF EXISTS `companies`
ADD COLUMN `deletion_status` BOOLEAN NOT NULL;
-- rollback ALTER TABLE IF EXISTS `companies` DROP COLUMN `deletion_status`;

-- changeset dzmitry.papkou:6
-- comment: add company functions table
CREATE TABLE `company_functions` (

    `id` UUID NOT NULL,
    `company_manager` BOOLEAN NOT NULL,
    `consumer` BOOLEAN NOT NULL,
    `company_claim_manager` BOOLEAN NOT NULL,
    `consumer_claim_manager` BOOLEAN NOT NULL,
    `company_setting_manager` BOOLEAN NOT NULL,
    `company_report_manager` BOOLEAN NOT NULL,
    
    CONSTRAINT `PK_COMPANY_FUNCTIONS`
    PRIMARY KEY (`id`),

    CONSTRAINT `FK_COMPANY_FUNCTIONS_COMPANY_ID`
    FOREIGN KEY (`id`)
    REFERENCES `companies` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
-- rollback DROP TABLE `company_functions`;