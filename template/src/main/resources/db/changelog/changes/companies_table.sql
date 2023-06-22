-- liquibase formatted sql
-- DROP SCHEMA IF EXISTS `insurance_manager_v1`;

-- DROP TABLE IF EXISTS `companies`;
CREATE TABLE `companies` (
    `id` UUID NOT NULL,
    `company_name` VARCHAR (35) NOT NULL,
    `country_code` VARCHAR (2) NOT NULL,
    `email` VARCHAR (100) NOT NULL,
    `site` VARCHAR (100),

    PRIMARY KEY (`id`)
);
-- rollback DROP TABLE `companies`;