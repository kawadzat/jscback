@echo off
echo Creating antivirus table...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita < create_antivirus_table.sql
echo Table creation completed!
pause 