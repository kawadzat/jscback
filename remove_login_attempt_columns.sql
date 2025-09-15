-- Remove login attempt tracking columns from users table
-- This script removes the columns added for brute force attack protection

USE securecapita;

-- Remove indexes first
DROP INDEX IF EXISTS idx_users_failed_attempts ON users;
DROP INDEX IF EXISTS idx_users_last_failed_login ON users;
DROP INDEX IF EXISTS idx_users_account_locked_until ON users;

-- Remove columns
ALTER TABLE users 
DROP COLUMN IF EXISTS failed_login_attempts;

ALTER TABLE users 
DROP COLUMN IF EXISTS last_failed_login;

ALTER TABLE users 
DROP COLUMN IF EXISTS account_locked_until;

-- Verify columns are removed
DESCRIBE users; 