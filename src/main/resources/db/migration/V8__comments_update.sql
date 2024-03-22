-- Add a new column to store the parent comment ID
ALTER TABLE blog_dbo.comments
    ADD COLUMN parent_comment_id UUID REFERENCES blog_dbo.comments(id);

-- Add a foreign key constraint to ensure referential integrity
ALTER TABLE blog_dbo.comments
    ADD CONSTRAINT fk_parent_comment
        FOREIGN KEY (parent_comment_id) REFERENCES blog_dbo.comments(id);

-- Add a new boolean column
ALTER TABLE blog_dbo.comments
    ADD COLUMN new_status BOOLEAN;

-- Update the new_status column based on the existing values in the status column
UPDATE blog_dbo.comments
SET new_status = (status = 'active');

-- Drop the old status column
ALTER TABLE blog_dbo.comments
DROP COLUMN status;

-- Rename the new_status column to status
ALTER TABLE blog_dbo.comments
    RENAME COLUMN new_status TO status;
