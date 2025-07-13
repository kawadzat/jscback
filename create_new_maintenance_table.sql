-- Create New Maintenance Table
-- This script creates a completely new maintenance table with proper defaults

-- Step 1: Disable foreign key checks
SET FOREIGN_KEY_CHECKS = 0;

-- Step 2: Drop the existing maintenance table completely
DROP TABLE IF EXISTS maintenance;

-- Step 3: Create a new maintenance table with proper defaults
CREATE TABLE maintenance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maintenance_type VARCHAR(50) NOT NULL DEFAULT 'PREVENTIVE',
    description TEXT NOT NULL DEFAULT 'Scheduled maintenance task',
    scheduled_date DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 1 DAY),
    completed_date DATETIME NULL,
    technician_name VARCHAR(255) NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    priority VARCHAR(50) NULL DEFAULT 'MEDIUM',
    notes TEXT NULL,
    laptop_id BIGINT NOT NULL,
    created_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NULL,
    last_modified_by VARCHAR(255) NULL,
    
    -- Foreign key constraint
    CONSTRAINT fk_maintenance_laptop FOREIGN KEY (laptop_id) REFERENCES laptop(id) ON DELETE CASCADE,
    
    -- Indexes for better performance
    INDEX idx_maintenance_laptop_id (laptop_id),
    INDEX idx_maintenance_status (status),
    INDEX idx_maintenance_type (maintenance_type),
    INDEX idx_maintenance_scheduled_date (scheduled_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Step 4: Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Step 5: Test the new table with minimal data
INSERT INTO maintenance (laptop_id) VALUES (1);

-- Step 6: Verify the test insert worked
SELECT 
    id,
    maintenance_type,
    description,
    scheduled_date,
    status,
    priority,
    laptop_id
FROM maintenance 
WHERE id = LAST_INSERT_ID();

-- Step 7: Show table structure
DESCRIBE maintenance;

-- Step 8: Show table creation statement
SHOW CREATE TABLE maintenance; 