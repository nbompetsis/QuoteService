CREATE TABLE IF NOT EXISTS QUOTE (
	id int NOT NULL AUTO_INCREMENT,
	author varchar(50),
	text varchar(255) NOT NULL,

	PRIMARY KEY (id)
);