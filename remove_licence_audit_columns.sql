-- Remove audit columns from licence table
-- This script should be run if the licence table already exists with audit columns

-- Drop foreign key constraints first (if they exist)
ALTER TABLE licence DROP FOREIGN KEY IF EXISTS licence_ibfk_2; -- created_by foreign key
ALTER TABLE licence DROP FOREIGN KEY IF EXISTS licence_ibfk_3; -- updated_by foreign key

-- Drop indexes for audit columns
DROP INDEX IF EXISTS idx_created_at ON licence;
DROP INDEX IF EXISTS idx_updated_at ON licence;
DROP INDEX IF EXISTS idx_created_by ON licence;
DROP INDEX IF EXISTS idx_updated_by ON licence;

-- Remove audit columns (with error handling for MySQL)
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'licence' 
     AND COLUMN_NAME = 'created_at') > 0,
    'ALTER TABLE licence DROP COLUMN created_at',
    'SELECT "created_at column does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'licence' 
     AND COLUMN_NAME = 'updated_at') > 0,
    'ALTER TABLE licence DROP COLUMN updated_at',
    'SELECT "updated_at column does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'licence' 
     AND COLUMN_NAME = 'created_by') > 0,
    'ALTER TABLE licence DROP COLUMN created_by',
    'SELECT "created_by column does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME = 'licence' 
     AND COLUMN_NAME = 'updated_by') > 0,
    'ALTER TABLE licence DROP COLUMN updated_by',
    'SELECT "updated_by column does not exist" as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt; 