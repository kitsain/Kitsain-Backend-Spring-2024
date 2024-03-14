CREATE TABLE blog_dbo.posts
(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title       TEXT         NOT NULL,
    description TEXT         NOT NULL,
    user_id     UUID         NOT NULL REFERENCES users(id),
    images      TEXT                    NULL,
    price       VARCHAR(255)            NULL,
    expring_date      TIMESTAMP         NULL,
    created_date       TIMESTAMP,
    modified_date      TIMESTAMP,
    created_by         VARCHAR(20),
    modified_by        VARCHAR(20)
);