-- Simple Maintenance Table Creation
-- This script creates a maintenance table without NOT NULL constraints

USE securecapita;

-- Drop existing table
DROP TABLE IF EXISTS maintenance;

-- Create simple table without NOT NULL constraints
CREATE TABLE maintenance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maintenance_type VARCHAR(50) DEFAULT 'PREVENTIVE',
    description TEXT DEFAULT 'Scheduled maintenance task',
    scheduled_date DATETIME DEFAULT (CURRENT_TIMESTAMP + INTERVAL 1 DAY),
    completed_date DATETIME NULL,
    technician_name VARCHAR(255) NULL,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    priority VARCHAR(50) DEFAULT 'MEDIUM',
    notes TEXT NULL,
    laptop_id BIGINT,
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

-- Test insert with minimal data
INSERT INTO maintenance (laptop_id) VALUES (1);

-- Verify the insert worked
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

-- Show table structure
DESCRIBE maintenance; 