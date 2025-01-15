CREATE TABLE Users(
    id bigint NOT NULL auto_increment,
    username varchar(64) NOT NULL UNIQUE,
    email varchar(64) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    registration_date datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    active tinyint DEFAULT 1 NOT NULL,

    PRIMARY KEY(id)
);