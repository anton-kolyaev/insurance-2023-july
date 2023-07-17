-- liquibase formatted sql

-- changeset dzmitry.papkou:7
-- comment: add users functions table
CREATE TABLE `user_functions` (

    `user_id` UUID NOT NULL,
    `company_id` UUID NOT NULL,
    `company_manager` BOOLEAN NOT NULL DEFAULT FALSE,
    `consumer` BOOLEAN NOT NULL DEFAULT FALSE,
    `company_claim_manager` BOOLEAN NOT NULL DEFAULT FALSE,
    `consumer_claim_manager` BOOLEAN NOT NULL DEFAULT FALSE,
    `company_setting_manager` BOOLEAN NOT NULL DEFAULT FALSE,
    `company_report_manager` BOOLEAN NOT NULL DEFAULT FALSE,
    
    CONSTRAINT `PK_USER_FUNCTIONS`
    PRIMARY KEY (`user_id`, `company_id`),

    CONSTRAINT `FK_USER_FUNCTIONS_USER_ID`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    CONSTRAINT `FK_USER_FUNCTIONS_COMPANY_ID`
    FOREIGN KEY (`company_id`)
    REFERENCES `companies` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
-- rollback DROP TABLE `user_functions`;