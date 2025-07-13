-- Check maintenance table structure
-- This script will help identify the current state of the maintenance table

-- Show table structure
DESCRIBE maintenance;

-- Show table creation statement
SHOW CREATE TABLE maintenance;

-- Check if table exists and has data
SELECT COUNT(*) as total_records FROM maintenance;

-- Check for any records with null description
SELECT COUNT(*) as null_description_count 
FROM maintenance 
WHERE description IS NULL OR description = '';

-- Show sample data
SELECT * FROM maintenance LIMIT 5;

-- Check column constraints
SELECT 
    COLUMN_NAME,
    IS_NULLABLE,
    DATA_TYPE,
    COLUMN_DEFAULT,
    COLUMN_KEY
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
AND TABLE_NAME = 'maintenance'
ORDER BY ORDINAL_POSITION; 