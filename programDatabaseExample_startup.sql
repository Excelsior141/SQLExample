CREATE SCHEMA IF NOT EXISTS `example`;
USE `example`;
CREATE TABLE IF NOT EXISTS `random` (`id` INT NOT NULL AUTO_INCREMENT, `random_int` INT NOT NULL, `random_double` DOUBLE NOT NULL, `random_string` VARCHAR(45) NOT NULL, `entry_date` DATETIME NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `id_UNIQUE` (`id` ASC));