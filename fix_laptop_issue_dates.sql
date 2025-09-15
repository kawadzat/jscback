-- Fix Laptop Issue Dates Script
-- This script identifies and fixes LAPTOP assets with null or invalid issue dates

-- Step 1: Identify LAPTOP assets with null issue dates
SELECT 
    id,
    assert_type,
    issue_date,
    serial_number,
    manufacturer,
    'NEEDS_FIX' as status
FROM laptop 
WHERE assert_type = 'LAPTOP' 
  AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');

-- Step 2: Count how many LAPTOP assets need fixing
SELECT 
    COUNT(*) as laptops_needing_issue_date_fix
FROM laptop 
WHERE assert_type = 'LAPTOP' 
  AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');

-- Step 3: Update LAPTOP assets with null issue dates to use purchase date or current date
-- Uncomment the following UPDATE statement after reviewing the affected records above

/*
UPDATE laptop 
SET issue_date = COALESCE(purchase_date, CURRENT_DATE)
WHERE assert_type = 'LAPTOP' 
  AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');
*/

-- Step 4: Verify the fix (run after the UPDATE)
SELECT 
    id,
    assert_type,
    issue_date,
    serial_number,
    manufacturer,
    'FIXED' as status
FROM laptop 
WHERE assert_type = 'LAPTOP' 
  AND issue_date IS NOT NULL 
  AND issue_date >= '2001-01-01';

-- Step 5: Final verification - should return 0 records
SELECT 
    COUNT(*) as remaining_laptops_with_invalid_issue_dates
FROM laptop 
WHERE assert_type = 'LAPTOP' 
  AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');

-- Additional useful queries:

-- Show all asset types and their issue date status
SELECT 
    assert_type,
    COUNT(*) as total_assets,
    SUM(CASE WHEN issue_date IS NULL THEN 1 ELSE 0 END) as null_issue_dates,
    SUM(CASE WHEN issue_date = '1970-01-01' THEN 1 ELSE 0 END) as zero_issue_dates,
    SUM(CASE WHEN issue_date < '2001-01-01' THEN 1 ELSE 0 END) as invalid_issue_dates
FROM laptop 
GROUP BY assert_type
ORDER BY assert_type;

-- Show LAPTOP assets with valid issue dates (for reference)
SELECT 
    id,
    assert_type,
    issue_date,
    serial_number,
    manufacturer
FROM laptop 
WHERE assert_type = 'LAPTOP' 
  AND issue_date IS NOT NULL 
  AND issue_date >= '2001-01-01'
ORDER BY issue_date DESC; 