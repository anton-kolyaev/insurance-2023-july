-- DROP SCHEMA IF EXISTS `java_internship`;
CREATE SCHEMA `java_internship`;

-- DROP TABLE IF EXISTS `users`;
CREATE TABLE `java_internship`.`users`(
`id` UUID NOT NULL,
`first_name` VARCHAR(35) NOT NULL,
`last_name` VARCHAR(35) NOT NULL,
`birthday` DATE NOT NULL,
`user_name` VARCHAR(35) NOT NULL,
`email` VARCHAR(100) NOT NULL,
`snn` VARCHAR(20) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE (`user_name`),
UNIQUE(`snn`)
);

-- DROP TABLE IF EXISTS `companies`;
CREATE TABLE `java_internship`.`companies` (
    `id` UUID NOT NULL,
    `company_name` VARCHAR (35) NOT NULL,
    `country_code` VARCHAR (2) NOT NULL,
    `email` VARCHAR (100) NOT NULL,
    `site` VARCHAR (100),

    PRIMARY KEY (`id`)
);

