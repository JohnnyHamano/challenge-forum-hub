CREATE TABLE Replies(
    id bigint NOT NULL auto_increment,
    topic_id bigint NOT NULL,
    author_id bigint NOT NULL,
    message varchar(255) NOT NULL,
    reply_date datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
    edited tinyint DEFAULT 0,
    last_edited datetime,
    active tinyint DEFAULT 1 NOT NULL,

    PRIMARY KEY(id),
    CONSTRAINT FK_reply_topic FOREIGN KEY(topic_id) REFERENCES topics(id),
    CONSTRAINT FK_reply_user FOREIGN KEY(author_id) REFERENCES users(id)
);