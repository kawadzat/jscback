-- Add password_last_changed column to users table
-- This column will track when the user's password was last changed
-- Used for implementing 90-day password expiration policy

ALTER TABLE users 
ADD COLUMN password_last_changed TIMESTAMP NULL;

-- Add comment to document the purpose
COMMENT ON COLUMN users.password_last_changed IS 'Timestamp when password was last changed. Used for 90-day password expiration policy.';

-- Optional: Set default value for existing users to current timestamp
-- This will require all existing users to change their password within 90 days
-- Uncomment the following line if you want to enforce this immediately:
-- UPDATE users SET password_last_changed = CURRENT_TIMESTAMP WHERE password_last_changed IS NULL;

-- Create index for performance when querying users with expiring passwords
CREATE INDEX idx_users_password_last_changed ON users(password_last_changed);

-- Optional: Create a view to easily find users with expiring passwords
CREATE OR REPLACE VIEW users_with_expiring_passwords AS
SELECT 
    id,
    first_name,
    last_name,
    email,
    password_last_changed,
    password_last_changed + INTERVAL '90 days' AS password_expiry_date,
    EXTRACT(DAYS FROM (password_last_changed + INTERVAL '90 days' - CURRENT_TIMESTAMP)) AS days_until_expiry
FROM users 
WHERE password_last_changed IS NOT NULL
  AND password_last_changed + INTERVAL '90 days' <= CURRENT_TIMESTAMP + INTERVAL '30 days' -- Expires within 30 days
ORDER BY password_expiry_date ASC;









