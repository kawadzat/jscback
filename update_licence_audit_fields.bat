@echo off
echo Updating licence table audit fields...
echo.

REM Set MySQL connection details
set MYSQL_HOST=localhost
set MYSQL_PORT=3306
set MYSQL_USER=root
set MYSQL_PASSWORD=
set MYSQL_DATABASE=securecapita

REM Run the SQL script
mysql -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% %MYSQL_DATABASE% < update_licence_audit_fields.sql

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Successfully updated licence table audit fields!
    echo The created_by and updated_by columns are now VARCHAR(255) instead of BIGINT.
) else (
    echo.
    echo Error updating licence table audit fields.
    echo Please check your MySQL connection and try again.
)

pause 