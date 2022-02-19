ALTER TABLE tg_user
    ALTER COLUMN chat_id SET NOT NULL;
ALTER TABLE tg_user
    ALTER COLUMN active SET NOT NULL;
ALTER TABLE tg_user
    ADD PRIMARY KEY (chat_id);

DROP TABLE IF EXISTS group_sub;
DROP TABLE IF EXISTS group_to_user;

CREATE TABLE group_sub
(
    id              INT NOT NULL,
    title           VARCHAR(100),
    last_article_id INT,
    PRIMARY KEY (id)
);

CREATE TABLE group_to_user
(
    group_id INT          NOT NULL,
    user_id  VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tg_user (chat_id),
    FOREIGN KEY (group_id) REFERENCES group_sub (id),
    UNIQUE (user_id, group_id)
);
