CREATE TABLE Topics(
    id bigint NOT NULL auto_increment,
    author_id bigint NOT NULL,
    category varchar(64) NOT NULL,
    title varchar(128) NOT NULL,
    message varchar(255) NOT NULL,
    creation_date datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    course varchar(64) NOT NULL,
    edited tinyint DEFAULT 0,
    last_edited datetime,
    active tinyint DEFAULT 1 NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT FK_topic_user FOREIGN KEY(author_id) REFERENCES users(id)
);