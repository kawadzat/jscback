@echo off
echo Making all fields nullable for non-laptop assets...
mysql -u root -pCaroline654321 -h localhost -P 3307 securecapita < make_ram_processor_nullable.sql
echo Migration completed!
pause 