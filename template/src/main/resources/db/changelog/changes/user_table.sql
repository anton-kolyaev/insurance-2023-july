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
`ssn` VARCHAR(20) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE (`user_name`),
UNIQUE(`ssn`)
);

