-- Fix the description field in the maintenance table
-- This script modifies the existing table to resolve the description field issue

USE securecapita;

-- Step 1: Modify the description column to be nullable with a default
ALTER TABLE maintenance 
MODIFY COLUMN description TEXT NULL DEFAULT 'Scheduled maintenance task';

-- Step 2: Update any existing null descriptions
UPDATE maintenance 
SET description = 'Scheduled maintenance task' 
WHERE description IS NULL OR description = '';

-- Step 3: Verify the changes
DESCRIBE maintenance;

-- Step 4: Test insert with null description
INSERT INTO maintenance (laptop_id, description) VALUES (1, NULL);

-- Step 5: Verify the test insert worked
SELECT 
    id,
    description,
    laptop_id
FROM maintenance 
WHERE id = LAST_INSERT_ID();

-- Step 6: Test insert with no description field
INSERT INTO maintenance (laptop_id) VALUES (1);

-- Step 7: Verify the second test insert worked
SELECT 
    id,
    description,
    laptop_id
FROM maintenance 
WHERE id = LAST_INSERT_ID(); 