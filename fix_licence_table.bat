@echo off
echo Fixing licence table by removing audit columns...
echo.

REM Set MySQL connection details
set MYSQL_HOST=localhost
set MYSQL_PORT=3306
set MYSQL_USER=root
set MYSQL_PASSWORD=
set MYSQL_DATABASE=securecapita

echo WARNING: This will drop and recreate the licence table!
echo All existing licence data will be lost!
echo.
set /p CONFIRM="Are you sure you want to continue? (y/N): "

if /i "%CONFIRM%"=="y" (
    echo.
    echo Running SQL script to recreate licence table...
    
    REM Run the SQL script
    mysql -h %MYSQL_HOST% -P %MYSQL_PORT% -u %MYSQL_USER% -p%MYSQL_PASSWORD% %MYSQL_DATABASE% < fix_licence_table.sql
    
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo Successfully recreated licence table without audit columns!
        echo The "Field 'created_at' doesn't have a default value" error should be resolved.
    ) else (
        echo.
        echo Error recreating licence table.
        echo Please check your MySQL connection and try again.
    )
) else (
    echo.
    echo Operation cancelled.
)

pause 