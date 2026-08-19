CREATE TABLE `person` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `first_name` varchar(255) NOT NULL,
                          `last_name` varchar(255) NOT NULL,
                          `adress` varchar(255) NOT NULL,
                          `gender` varchar(255) NOT NULL,
                          PRIMARY KEY (`id`)
);

CREATE TABLE `books` (
                         `id` INT AUTO_INCREMENT PRIMARY KEY,
                         `author` varchar(100),
                         `launch_date` datetime NOT NULL,
                         `price` float(10,2) NOT NULL,
                         `title` varchar(100)
);


