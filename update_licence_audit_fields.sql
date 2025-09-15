-- Update licence table to change audit fields from BIGINT to VARCHAR(255)
-- This script should be run if the licence table already exists with BIGINT audit fields

-- Drop foreign key constraints first
ALTER TABLE licence DROP FOREIGN KEY IF EXISTS licence_ibfk_2; -- created_by foreign key
ALTER TABLE licence DROP FOREIGN KEY IF EXISTS licence_ibfk_3; -- updated_by foreign key

-- Change column types
ALTER TABLE licence MODIFY COLUMN created_by VARCHAR(255);
ALTER TABLE licence MODIFY COLUMN updated_by VARCHAR(255);

-- Add indexes for the new VARCHAR columns
CREATE INDEX IF NOT EXISTS idx_created_by ON licence(created_by);
CREATE INDEX IF NOT EXISTS idx_updated_by ON licence(updated_by);

-- Update existing records to set default values if needed
UPDATE licence SET created_by = 'system' WHERE created_by IS NULL;
UPDATE licence SET updated_by = 'system' WHERE updated_by IS NULL; 