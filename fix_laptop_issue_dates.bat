@echo off
echo ========================================
echo Fix Laptop Issue Dates Script
echo ========================================
echo.
echo This script will identify and fix LAPTOP assets with null or invalid issue dates.
echo.
echo Step 1: Identifying LAPTOP assets with null/invalid issue dates...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "SELECT id, assert_type, issue_date, serial_number, manufacturer, 'NEEDS_FIX' as status FROM laptop WHERE assert_type = 'LAPTOP' AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');"

echo.
echo Step 2: Counting affected records...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "SELECT COUNT(*) as laptops_needing_issue_date_fix FROM laptop WHERE assert_type = 'LAPTOP' AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');"

echo.
echo Step 3: Updating LAPTOP assets with null/invalid issue dates...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "UPDATE laptop SET issue_date = COALESCE(purchase_date, CURRENT_DATE) WHERE assert_type = 'LAPTOP' AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');"

echo.
echo Step 4: Verifying the fix...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "SELECT COUNT(*) as remaining_laptops_with_invalid_issue_dates FROM laptop WHERE assert_type = 'LAPTOP' AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');"

echo.
echo Step 5: Final verification - should show 0 records...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "SELECT COUNT(*) as final_check FROM laptop WHERE assert_type = 'LAPTOP' AND (issue_date IS NULL OR issue_date = '1970-01-01' OR issue_date < '2001-01-01');"

echo.
echo ========================================
echo Script completed!
echo ========================================
echo.
echo If the final check shows 0 records, all LAPTOP assets now have valid issue dates.
echo If it shows more than 0, please review the data manually.
echo.
pause 