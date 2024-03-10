CREATE TABLE blog_dbo.users (
    id UUID  PRIMARY KEY DEFAULT uuid_generate_v4(),
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NULL,
    name     VARCHAR(255) NOT NULL,
    active   boolean NULL DEFAULT false,
    provider VARCHAR(20) NOT NULL DEFAULT 'LOCAL',
    is_pending   boolean NOT NULL DEFAULT false,
    created_date       TIMESTAMP,
    modified_date      TIMESTAMP,
    created_by         VARCHAR(20),
    modified_by        VARCHAR(20)
);