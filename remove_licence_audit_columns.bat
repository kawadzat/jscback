@echo off
echo Removing audit columns from licence table...
echo.

REM Set MySQL connection details
set MYSQL_HOST=localhost
set MYSQL_PORT=3306
set MYSQL_USER=root
set MYSQL_PASSWORD=
set MYSQL_DATABASE=securecapita

REM Run the SQL script
mysql -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% %MYSQL_DATABASE% < remove_licence_audit_columns.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Successfully removed audit columns from licence table!
    echo The created_at, updated_at, created_by, and updated_by columns have been removed.
) else (
    echo.
    echo Error removing audit columns from licence table.
    echo Please check your MySQL connection and try again.
)

pause 