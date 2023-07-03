-- liquibase formatted sql


-- changeset Dovidas Zablockis:1 contextFilter:test
-- comment: populate users table
INSERT INTO `users` (
    `username`,
    `password`,
    `email`
) VALUES (
    'viewer',
    '$2a$10$z7w5ZASKP6Ic/j4rGq6e3u19z3l/4pwpuGdN3OOnEwUU9dnbTnIaW',
    'viewer@coherentsolutions.com'
);

INSERT INTO `users` (
    `username`,
    `password`,
    `email`,
    `role`
) VALUES (
    'moderator',
    '$2a$10$adep3.38anG5LXIkrYFEFO0T7oxspRjsplB7II7NzlkAWVkf8sWZy',
    'moderator@coherentsolutions.com',
    'MODERATOR'
);

INSERT INTO `users` (
    `username`,
    `password`,
    `email`,
    `role`
) VALUES (
    'admin',
    '$2a$10$zPUn5HuPe89HuvhlSqSUZunnlDXWFbcMvFO9jyLc3FiOKam0M4M/W',
    'admin@coherentsolutions.com',
    'ADMIN'
);
-- rollback DELETE FROM `users` WHERE `username` = 'viewer';
-- rollback DELETE FROM `users` WHERE `username` = 'moderator';
-- rollback DELETE FROM `users` WHERE `username` = 'admin';


-- changeset Dovidas Zablockis:2 contextFilter:test
-- comment: populate user profiles table
INSERT INTO `user_profiles` (
    `user_id`,
    `first_name`,
    `last_name`,
    `birthday`,
    `ssn`
) VALUES (
    SELECT `id` FROM `users` WHERE `username` = 'admin',
    'viewer_firstname',
    'viewer_lastname',
    DATE '2003-06-12',
    '54549854984LT'
);

INSERT INTO `user_profiles` (
    `user_id`,
    `first_name`,
    `last_name`,
    `birthday`,
    `ssn`
) VALUES (
    SELECT `id` FROM `users` WHERE `username` = 'moderator',
    'moderator_firstname',
    'moderator_lastname',
    DATE '2008-07-24',
    '64549815984LT'
);

INSERT INTO `user_profiles` (
    `user_id`,
    `first_name`,
    `last_name`,
    `birthday`,
    `ssn`
) VALUES (
    SELECT `id` FROM `users` WHERE `username` = 'viewer',
    'viewer_firstname',
    'viewer_lastname',
    DATE '1997-10-11',
    '44789320479LT'
);
-- rollback DELETE FROM `user_profiles` WHERE `first_name` = 'admin';
-- rollback DELETE FROM `user_profiles` WHERE `first_name` = 'viewer';