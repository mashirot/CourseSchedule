CREATE TABLE IF NOT EXISTS `user` (
    `user_code` varchar(30) NOT NULL,
    `user_password` char(32) NOT NULL,
    `password_salt` char(50) NOT NULL,
    `user_nickname` varchar(12) DEFAULT NULL,
    `user_term_initial_date` date NOT NULL,
    `user_table_name` varchar(33) NOT NULL,
    `user_api_token` char(32) NOT NULL,
    PRIMARY KEY (`user_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
