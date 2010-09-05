CREATE DATABASE smscactus CHARSET UTF8;

USE smscactus;

CREATE TABLE messages (
	id INTEGER PRIMARY KEY,
	address varchar(25) not null,
	body varchar(1024) not null,
	timestamp DATETIME NOT NULL,
	incoming BIT not null
) CHARSET UTF8;

CREATE TABLE sendlist (
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	address varchar(25) not null,
	body varchar(1024) not null 
) CHARSET UTF8;

CREATE TABLE c2dm (
	`key` VARCHAR(24) PRIMARY KEY,
	value VARCHAR(1024) NOT NULL
);

CREATE USER 'smscactus'@'localhost' IDENTIFIED BY 'smscactus';
GRANT SELECT, INSERT, DELETE ON sendlist TO 'smscactus'@'localhost';
GRANT SELECT, INSERT, UPDATE ON messages TO 'smscactus'@'localhost';
GRANT SELECT, INSERT, UPDATE on c2dm TO 'smscactus'@'localhost';
