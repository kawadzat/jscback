-- Fix licence table by removing audit columns
-- This script will handle the "Field 'created_at' doesn't have a default value" error

-- First, let's check if the table exists and what columns it has
SELECT 'Current licence table structure:' as info;
DESCRIBE licence;

-- Drop the existing licence table completely
DROP TABLE IF EXISTS licence;

-- Recreate the licence table without audit columns
CREATE TABLE licence (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    license_key VARCHAR(255) NOT NULL UNIQUE,
    purchase_date TIMESTAMP NULL,
    installation_date TIMESTAMP NULL,
    expiry_date TIMESTAMP NULL,
    license_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    supplier VARCHAR(100),
    vendor VARCHAR(100),
    number_of_seats INT,
    price DECIMAL(10,2),
    currency VARCHAR(50),
    version VARCHAR(100),
    installation_path VARCHAR(255),
    file_path VARCHAR(255),
    assigned_to VARCHAR(100),
    assigned_email VARCHAR(255),
    department VARCHAR(100),
    station VARCHAR(100),
    notes VARCHAR(1000),
    laptop_id BIGINT,
    
    -- Foreign key constraints
    FOREIGN KEY (laptop_id) REFERENCES laptop(id) ON DELETE SET NULL,
    
    -- Indexes for better performance
    INDEX idx_license_key (license_key),
    INDEX idx_license_name (license_name),
    INDEX idx_license_type (license_type),
    INDEX idx_status (status),
    INDEX idx_supplier (supplier),
    INDEX idx_vendor (vendor),
    INDEX idx_assigned_to (assigned_to),
    INDEX idx_department (department),
    INDEX idx_station (station),
    INDEX idx_laptop_id (laptop_id),
    INDEX idx_expiry_date (expiry_date)
);

-- Add comments to the table
ALTER TABLE licence COMMENT = 'Software license management table';

SELECT 'Licence table recreated successfully without audit columns!' as success; 