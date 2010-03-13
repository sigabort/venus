DROP DATABASE IF EXISTS `venustest`;

CREATE DATABASE `venustest` CHARACTER SET utf8;

GRANT ALL PRIVILEGES ON venustest.* TO 'venus'@'localhost'  IDENTIFIED BY 'venus';

USE `venustest`;
