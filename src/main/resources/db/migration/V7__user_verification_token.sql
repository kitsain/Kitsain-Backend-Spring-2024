CREATE TABLE blog_dbo.user_verification_token
(
    id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id            UUID         NOT NULL REFERENCES users (id),
    verification_token VARCHAR(255),
    exp_date           TIMESTAMP,
    created_date       TIMESTAMP,
    modified_date      TIMESTAMP,
    created_by         VARCHAR(20),
    modified_by        VARCHAR(20)
);