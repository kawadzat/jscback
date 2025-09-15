@echo off
echo Adding password_last_changed column to users table...
echo This implements 90-day password expiration policy
echo.

REM Replace with your actual database connection details
REM Example for PostgreSQL:
REM psql -h localhost -U your_username -d your_database -f add_password_last_changed_column.sql

REM Example for MySQL:
REM mysql -h localhost -u your_username -p your_database < add_password_last_changed_column.sql

echo Please run the SQL script manually with your database client:
echo File: add_password_last_changed_column.sql
echo.
echo For PostgreSQL:
echo psql -h localhost -U your_username -d your_database -f add_password_last_changed_column.sql
echo.
echo For MySQL:
echo mysql -h localhost -u your_username -p your_database ^< add_password_last_changed_column.sql
echo.
pause



