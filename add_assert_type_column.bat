@echo off
echo Adding assert_type column to laptop table...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita < add_assert_type_column.sql
echo Column addition completed!
pause  