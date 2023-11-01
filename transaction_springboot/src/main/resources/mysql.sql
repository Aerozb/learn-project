CREATE TABLE `user` (
                        `id` int(11) NOT NULL,
                        `name` varchar(25) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_point` (
                              `user_id` int(11) NOT NULL,
                              `point` int(11) DEFAULT NULL,
                              PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;