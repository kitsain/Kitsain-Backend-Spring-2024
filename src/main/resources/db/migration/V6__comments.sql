CREATE TABLE blog_dbo.comments
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    content     TEXT      NOT NULL,
    status      VARCHAR(255) NOT NULL,
    user_id UUID,
    post_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    created_date       TIMESTAMP,
    modified_date      TIMESTAMP,
    created_by         VARCHAR(20),
    modified_by        VARCHAR(20)
);