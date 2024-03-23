CREATE TABLE blog_dbo.favorites
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id       UUID NOT NULL,
    post_id       UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    created_date  TIMESTAMP,
    modified_date TIMESTAMP,
    created_by    VARCHAR(20),
    modified_by   VARCHAR(20)
);

ALTER TABLE blog_dbo.posts
    ADD COLUMN favourite BIGINT NOT NULL DEFAULT 0;
