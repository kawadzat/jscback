-- Create licence table for software license management
CREATE TABLE IF NOT EXISTS licence (
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

-- Add comments to the table and columns
ALTER TABLE licence COMMENT = 'Software license management table';

-- Sample data for testing (optional)
INSERT INTO licence (
    license_name, 
    description, 
    license_key, 
    license_type, 
    status, 
    supplier, 
    vendor, 
    price, 
    currency, 
    version
) VALUES 
('Microsoft Office 365', 'Office productivity suite', 'MS-365-001-2024', 'SUBSCRIPTION', 'ACTIVE', 'Microsoft', 'Microsoft', 99.99, 'USD', '2024'),
('Adobe Creative Suite', 'Design and creative software', 'ADOBE-CS-001-2024', 'SUBSCRIPTION', 'ACTIVE', 'Adobe', 'Adobe', 599.99, 'USD', '2024'),
('Windows 11 Pro', 'Operating system license', 'WIN11-PRO-001-2024', 'PERPETUAL', 'ACTIVE', 'Microsoft', 'Microsoft', 199.99, 'USD', '11.0'),
('Antivirus Pro', 'Security software', 'AV-PRO-001-2024', 'SUBSCRIPTION', 'ACTIVE', 'SecurityCorp', 'SecurityCorp', 49.99, 'USD', '2024'),
('Database Server', 'Database management system', 'DB-SERVER-001-2024', 'PERPETUAL', 'ACTIVE', 'DatabaseCorp', 'DatabaseCorp', 999.99, 'USD', '2024');

-- Create view for license statistics
CREATE OR REPLACE VIEW licence_statistics AS
SELECT 
    COUNT(*) as total_licences,
    COUNT(CASE WHEN status = 'ACTIVE' THEN 1 END) as active_licences,
    COUNT(CASE WHEN status = 'EXPIRED' THEN 1 END) as expired_licences,
    COUNT(CASE WHEN status = 'INACTIVE' THEN 1 END) as inactive_licences,
    COUNT(CASE WHEN laptop_id IS NOT NULL THEN 1 END) as assigned_licences,
    COUNT(CASE WHEN laptop_id IS NULL THEN 1 END) as unassigned_licences,
    COUNT(CASE WHEN expiry_date < NOW() THEN 1 END) as expired_by_date,
    COUNT(CASE WHEN expiry_date BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 DAY) THEN 1 END) as expiring_soon,
    SUM(CASE WHEN price IS NOT NULL THEN price ELSE 0 END) as total_value
FROM licence;

-- Create view for license assignment overview
CREATE OR REPLACE VIEW licence_assignment_overview AS
SELECT 
    l.id as laptop_id,
    l.serial_number as laptop_serial,
    l.manufacturer as laptop_manufacturer,
    l.status as laptop_status,
    COUNT(lic.id) as assigned_licence_count,
    SUM(CASE WHEN lic.price IS NOT NULL THEN lic.price ELSE 0 END) as total_licence_value
FROM laptop l
LEFT JOIN licence lic ON l.id = lic.laptop_id
GROUP BY l.id, l.serial_number, l.manufacturer, l.status
ORDER BY assigned_licence_count DESC; 