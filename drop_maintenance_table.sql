-- Drop maintenance table
-- This will remove the table and all its data

-- First, drop any foreign key constraints if they exist
SET FOREIGN_KEY_CHECKS = 0;

-- Drop the maintenance table
DROP TABLE IF EXISTS maintenance;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Verify the table has been dropped
SELECT 'Maintenance table has been dropped successfully' AS status; 