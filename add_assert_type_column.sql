-- Add assert_type column to laptop table
ALTER TABLE laptop ADD COLUMN assert_type VARCHAR(100) NOT NULL DEFAULT 'LAPTOP';

-- Update existing records with a default value
UPDATE laptop SET assert_type = 'LAPTOP' WHERE assert_type IS NULL OR assert_type = '';

-- Add comment to the column
ALTER TABLE laptop MODIFY COLUMN assert_type VARCHAR(100) NOT NULL COMMENT 'Type of asset (e.g., LAPTOP, DESKTOP, TABLET, etc.)'; 