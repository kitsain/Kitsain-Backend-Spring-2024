ALTER TABLE blog_dbo.posts
    ADD COLUMN status BOOLEAN DEFAULT true; -- Adding a new column 'status' with a default value of true
UPDATE blog_dbo.posts
SET status = true;
