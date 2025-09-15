-- Update laptopAcknowledgment table to fix notes column length
-- This script ensures the notes column is limited to 500 characters

-- First, truncate any existing notes that are longer than 500 characters
UPDATE laptopAcknowledgment 
SET notes = LEFT(notes, 500) 
WHERE LENGTH(notes) > 500;

-- Then modify the column to have the correct length limit
ALTER TABLE laptopAcknowledgment 
MODIFY COLUMN notes VARCHAR(500);

-- Also ensure other columns have proper length limits
ALTER TABLE laptopAcknowledgment 
MODIFY COLUMN signature_type VARCHAR(50),
MODIFY COLUMN ip_address VARCHAR(45),
MODIFY COLUMN user_agent VARCHAR(500),
MODIFY COLUMN certificate_info VARCHAR(1000),
MODIFY COLUMN signature_hash VARCHAR(255); 