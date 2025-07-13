-- Fix maintenance table structure
-- Drop existing table if it has wrong structure
DROP TABLE IF EXISTS maintenance;

-- Recreate maintenance table with proper constraints
CREATE TABLE maintenance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    maintenance_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    scheduled_date DATETIME NOT NULL,
    completed_date DATETIME NULL,
    technician_name VARCHAR(255) NULL,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NULL,
    notes TEXT NULL,
    laptop_id BIGINT NOT NULL,
    created_date DATETIME NULL,
    last_modified_date DATETIME NULL,
    created_by VARCHAR(255) NULL,
    last_modified_by VARCHAR(255) NULL,
    
    -- Foreign key constraint
    CONSTRAINT fk_maintenance_laptop FOREIGN KEY (laptop_id) REFERENCES laptop(id) ON DELETE CASCADE,
    
    -- Indexes for better performance
    INDEX idx_maintenance_laptop_id (laptop_id),
    INDEX idx_maintenance_status (status),
    INDEX idx_maintenance_type (maintenance_type),
    INDEX idx_maintenance_scheduled_date (scheduled_date)
);

-- Insert sample data for testing
INSERT INTO maintenance (maintenance_type, description, scheduled_date, completed_date, technician_name, status, priority, notes, laptop_id) 
VALUES ('PREVENTIVE', 'Sample maintenance task', '2025-01-15 10:00:00', NULL, 'John Doe', 'SCHEDULED', 'MEDIUM', 'Sample maintenance record', 1); 