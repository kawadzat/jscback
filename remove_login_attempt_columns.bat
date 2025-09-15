@echo off
echo Removing login attempt tracking columns from users table...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita < remove_login_attempt_columns.sql
echo Login attempt columns removed successfully!
pause 