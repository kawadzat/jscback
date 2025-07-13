-- Force JPA to recreate the maintenance table with proper defaults
-- This script ensures the table is created with the correct schema

USE securecapita;

-- Step 1: Completely drop the maintenance table
DROP TABLE IF EXISTS maintenance;

-- Step 2: Drop any related sequences or constraints
SET FOREIGN_KEY_CHECKS = 0;

-- Step 3: Create the table manually with proper defaults (JPA will use this as reference)
CREATE TABLE maintenance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maintenance_type VARCHAR(50) NOT NULL DEFAULT 'PREVENTIVE',
    description TEXT DEFAULT 'Scheduled maintenance task',
    scheduled_date DATETIME NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 1 DAY),
    completed_date DATETIME NULL,
    technician_name VARCHAR(255) NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED',
    priority VARCHAR(50) DEFAULT 'MEDIUM',
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

SET FOREIGN_KEY_CHECKS = 1;

-- Step 4: Test the table with minimal data
INSERT INTO maintenance (laptop_id) VALUES (1);

-- Step 5: Verify the defaults work
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

-- Step 6: Show table structure
DESCRIBE maintenance; 