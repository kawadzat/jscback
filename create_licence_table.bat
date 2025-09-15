@echo off
echo ========================================
echo Creating Licence Table
echo ========================================
echo.
echo This script will create the licence table and related database objects.
echo.
echo Step 1: Creating licence table...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita < create_licence_table.sql

echo.
echo Step 2: Verifying table creation...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "DESCRIBE licence;"

echo.
echo Step 3: Checking sample data...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "SELECT id, license_name, license_type, status, supplier FROM licence LIMIT 5;"

echo.
echo Step 4: Checking statistics view...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita -e "SELECT * FROM licence_statistics;"

echo.
echo ========================================
echo Licence table creation completed!
echo ========================================
echo.
pause 