-- Remove notes column length limit
-- This script changes the notes column from VARCHAR(500) to TEXT to allow unlimited length

ALTER TABLE laptopAcknowledgment 
MODIFY COLUMN notes TEXT; 