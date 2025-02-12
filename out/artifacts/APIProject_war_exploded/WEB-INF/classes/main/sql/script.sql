DROP TABLE IF EXISTS `device`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(10) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
INSERT INTO `role` (`role_id`, `role_name`) VALUES
(0, 'admin'),
(1, 'client'),
(2, 'moderator');
CREATE TABLE `user` (
  `user_id` bigint(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(10) DEFAULT NULL,
  `password` varchar(150) DEFAULT NULL,
  `first_name` varchar(20) DEFAULT NULL,
  `last_name` varchar(20) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  `picture` varchar(1200) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
INSERT INTO `user` (`login`, `password`, `first_name`, `last_name`, `role_id`, `picture`, `email`) VALUES
('admin', '3ca9c4ab77a2deec98db4cf0803bee0ce2ccef10c1b0f872221f8e64da95f21632e263d0c8d5f15fe20daec58fb43a41ec13230abe2a12129d8ac43e15bc8734', 'Ivan', 'Ivanov', 0, NULL, 'vladbogun@gmail.com'),
('user1', '3ca9c4ab77a2deec98db4cf0803bee0ce2ccef10c1b0f872221f8e64da95f21632e263d0c8d5f15fe20daec58fb43a41ec13230abe2a12129d8ac43e15bc8734', 'Petr', 'Petrov', 1, NULL, 'vladbogun@gmail.com'),
('user2', '3ca9c4ab77a2deec98db4cf0803bee0ce2ccef10c1b0f872221f8e64da95f21632e263d0c8d5f15fe20daec58fb43a41ec13230abe2a12129d8ac43e15bc8734', 'Вячеслав', 'Соколов', 1, NULL, 'vladbogun@gmail.com'),
('user3', '3ca9c4ab77a2deec98db4cf0803bee0ce2ccef10c1b0f872221f8e64da95f21632e263d0c8d5f15fe20daec58fb43a41ec13230abe2a12129d8ac43e15bc8734', 'Светлана', 'Казакова', 1, NULL, 'vladbogun@gmail.com'),
('user4', '3ca9c4ab77a2deec98db4cf0803bee0ce2ccef10c1b0f872221f8e64da95f21632e263d0c8d5f15fe20daec58fb43a41ec13230abe2a12129d8ac43e15bc8734', 'Светлана', 'Казакова', 1, NULL, 'vladbogun@gmail.com'),
('user5', '3ca9c4ab77a2deec98db4cf0803bee0ce2ccef10c1b0f872221f8e64da95f21632e263d0c8d5f15fe20daec58fb43a41ec13230abe2a12129d8ac43e15bc8734', 'Карл', 'Шафранек', 1, NULL, 'vladbogun@gmail.com');
CREATE TABLE `device` (
  `device_id` bigint(12) ZEROFILL NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) DEFAULT NULL,
  `temperature` int(10) DEFAULT NULL,
  `humidity` int(10) DEFAULT NULL,
  `last_date_time` DATETIME DEFAULT NULL,
  `user_id` bigint(11) NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;
INSERT INTO `device` (`user_id`) VALUES
(2),
(2),
(3),
(4),
(5),
(6),
(6),
(3),
(3),
(3),
(3);