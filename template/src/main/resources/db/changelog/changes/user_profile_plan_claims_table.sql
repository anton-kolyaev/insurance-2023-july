-- liquibase formatted sql

-- changeset dovias:1
CREATE TABLE `insurance_manager_v1`.`user_profile_plan_claims` (
    `user_id` UUID NOT NULL,
    `plan_id` UUID NOT NULL,
    `date` DATE NOT NULL,
    `number` VARCHAR(16) NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL,
    `status` ENUM('APPROVED', 'DECLINED', 'PENDING', 'HOLD') NOT NULL,

    FOREIGN KEY (`user_id`)
    -- comment: if you're getting syntax error on an IDE on `users` table name,
    -- comment: ignore it, its due to the fact that the definition is in another file.
    REFERENCES `insurance_manager_v1`.`users` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`plan_id`)
    -- comment: if you're getting syntax error on an IDE on `company_package_plans` table name,
    -- comment: ignore it, its due to the fact that the definition is in another file.
    REFERENCES `insurance_manager_v1`.`company_package_plans` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
)
-- rollback DROP TABLE `user_profile_plan_claims`;