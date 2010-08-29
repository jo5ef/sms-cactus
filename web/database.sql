CREATE TABLE messages (
	id INTEGER PRIMARY KEY,
	address varchar(25) not null,
	body varchar(1024) not null,
	timestamp DATETIME NOT NULL,
	incoming BIT not null
);

CREATE TABLE sendlist (
	id INTEGER PRIMARY KEY AUTO_INCREMENT,
	address varchar(25) not null,
	body varchar(1024) not null
);
