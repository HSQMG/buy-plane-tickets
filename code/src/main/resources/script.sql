#Create the database
DROP DATABASE IF EXISTS `flight_manager`;
CREATE DATABASE `flight_manager`;

#Create user and grante permission on the created database
DROP USER IF EXISTS '21120075_21120556'@'localhost';
CREATE USER '21120075_21120556'@'localhost' IDENTIFIED BY '21120075_21120556';
GRANT ALL PRIVILEGES ON `flight_manager`.* TO '21120075_21120556'@'localhost';

USE `flight_manager`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`id` int(11) NOT NULL AUTO_INCREMENT,
  	`user_name` varchar(70) NOT NULL,
  	`encrypted_password` varchar(70) NOT NULL,
  	`role` varchar(15) DEFAULT NULL,
  	
  	UNIQUE (`user_name`),
 	 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `base_account`;
CREATE TABLE `base_account` (
 	`id` int(11) NOT NULL,
 
  	CONSTRAINT `fk_base_account_user` FOREIGN KEY(`id`) REFERENCES user(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `customer_account`;
CREATE TABLE `customer_account` (
  	`id` int(11) NOT NULL,
 	`name` nvarchar(70) DEFAULT NULL,
	`identity_code` nvarchar(15) DEFAULT NULL,
	`phone_number` nvarchar(15) DEFAULT NULL,
 	 CONSTRAINT `fk_customer_base` FOREIGN KEY(`id`) REFERENCES base_account(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `manager_account`;
CREATE TABLE `manager_account` (
  	`id` int(11) NOT NULL,
 	`name` nvarchar(70) DEFAULT NULL,
	`identity_code` nvarchar(15) DEFAULT NULL,
	`phone_number` nvarchar(15) DEFAULT NULL,
 	 CONSTRAINT `fk_manager_base` FOREIGN KEY(`id`) REFERENCES base_account(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `admin_account`;
CREATE TABLE `admin_account` (
  	`id` int(11) NOT NULL,
 	`name` nvarchar(70) DEFAULT NULL,
	`identity_code` nvarchar(15) DEFAULT NULL,
	`phone_number` nvarchar(15) DEFAULT NULL,
 	 CONSTRAINT `fk_admin_base` FOREIGN KEY(`id`) REFERENCES base_account(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `airport`;
CREATE TABLE `airport` (
  	`id` int(11) NOT NULL AUTO_INCREMENT,
 	`name` nvarchar(70) DEFAULT NULL,
	
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `ticket_class`;
CREATE TABLE `ticket_class` (
  	`id` int(11) NOT NULL AUTO_INCREMENT,
 	`name` nvarchar(70) DEFAULT NULL,
	
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `flight`;
CREATE TABLE `flight` (
  	`id` int(11) NOT NULL AUTO_INCREMENT,
 	`departure_airport_id` int(11) DEFAULT NULL,
	`arrival_airport_id` int(11) DEFAULT NULL,
	`date_time` date DEFAULT NULL,
	
 	CONSTRAINT `fk_flight_departure` FOREIGN KEY(`departure_airport_id`) REFERENCES airport(`id`),
	CONSTRAINT `fk_flight_arrival` FOREIGN KEY(`arrival_airport_id`) REFERENCES airport(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `transition_airport`;
CREATE TABLE `transition_airport` (
  	`id` int(11) NOT NULL AUTO_INCREMENT,
	`flight_id` int(11) NOT NULL,
 	`airport_id` int(11) DEFAULT NULL,
	`transition_time` int(15) DEFAULT NULL,
	`note` nvarchar(70) DEFAULT NULL,

 	CONSTRAINT `fk_transition_airport` FOREIGN KEY(`airport_id`) REFERENCES airport(`id`),
	CONSTRAINT `fk_transition_flight` FOREIGN KEY(`flight_id`) REFERENCES flight(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `flight_detail`;
CREATE TABLE `flight_detail` (
  	`flight_id` int(11) NOT NULL,
	`flight_time` int(11) DEFAULT 0,
	`first_class_seat_size` int(11) DEFAULT 0,
	`second_class_seat_size` int(11) DEFAULT 0,
	`first_class_seat_price` int(11) DEFAULT 0,
	`second_class_seat_price` int(11) DEFAULT 0,

 	CONSTRAINT `fk_flight_detail` FOREIGN KEY(`flight_id`) REFERENCES flight(`id`),
  	PRIMARY KEY (`flight_id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  	`id` int(11) NOT NULL AUTO_INCREMENT,
 	`flight_id` int(11) NOT NULL,
	`ticket_class_id` int(11) NOT NULL,
	`price` int(11) DEFAULT 0,
	`name` nvarchar(70) DEFAULT NULL,
	`identity_code` nvarchar(15) DEFAULT NULL,
	`phone_number` nvarchar(15) DEFAULT NULL,
	`is_booked` boolean DEFAULT FALSE,	

 	CONSTRAINT `fk_ticket_flight` FOREIGN KEY(`flight_id`) REFERENCES flight(`id`),
	CONSTRAINT `fk_ticket_ticket_class` FOREIGN KEY(`ticket_class_id`) REFERENCES ticket_class(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE `reservation` (
  	`id` int(11) NOT NULL,
	`account_id` int(11) DEFAULT NULL,
	`booking_date` date DEFAULT NULL,

 	CONSTRAINT `fk_reservation_ticket` FOREIGN KEY(`id`) REFERENCES ticket(`id`),
	CONSTRAINT `fk_reservation_customer` FOREIGN KEY(`account_id`) REFERENCES base_account(`id`),
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `policy`;
CREATE TABLE `policy` (
  	`id` int(11) NOT NULL AUTO_INCREMENT,
	`name` nvarchar(70) DEFAULT NULL,
 	`datatype` varchar(15) DEFAULT NULL,
	`value` varchar(70) DEFAULT NULL,
	`is_applied` boolean DEFAULT FALSE,	

  	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;

#Admin with username = 'admin' and password = hashing('admin')
INSERT INTO `user` VALUES(1, 'admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'Admin');
INSERT INTO `base_account` VALUES(1);
INSERT INTO `admin_account` VALUES(1, NULL, NULL, NULL);

#Insert default ticket class
INSERT INTO `ticket_class` VALUES(1, "1");
INSERT INTO `ticket_class` VALUES(2, "2");