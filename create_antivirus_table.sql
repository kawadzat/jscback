-- Create antivirus table
CREATE TABLE IF NOT EXISTS antivirus (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    `key` VARCHAR(255) NOT NULL UNIQUE,
    renew_time_interval INTEGER,
    version VARCHAR(255),
    vendor VARCHAR(255),
    status VARCHAR(50),
    is_installed BOOLEAN DEFAULT FALSE,
    license_expiration_date TIMESTAMP NULL,
    last_scan_date TIMESTAMP NULL,
    laptop_id BIGINT NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    last_modified_by VARCHAR(255),
    FOREIGN KEY (laptop_id) REFERENCES laptop(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_antivirus_laptop_id ON antivirus(laptop_id);
CREATE INDEX idx_antivirus_key ON antivirus(`key`);
CREATE INDEX idx_antivirus_vendor ON antivirus(vendor);
CREATE INDEX idx_antivirus_status ON antivirus(status);
CREATE INDEX idx_antivirus_license_expiration ON antivirus(license_expiration_date);
CREATE INDEX idx_antivirus_last_scan_date ON antivirus(last_scan_date);

-- Insert sample data (optional)
INSERT INTO antivirus (name, `key`, renew_time_interval, version, vendor, status, is_installed, license_expiration_date, last_scan_date, laptop_id, created_by) 
VALUES 
('Norton Antivirus Plus', 'NORTON-2024-ABC123', 365, '2024.1.0', 'Norton', 'ACTIVE', true, '2025-12-31 23:59:59', '2024-01-15 10:30:00', 1, 'system'),
('McAfee Total Protection', 'MCAFEE-2024-XYZ789', 365, '2024.2.0', 'McAfee', 'ACTIVE', true, '2025-06-30 23:59:59', '2024-01-20 14:30:00', 1, 'system')
ON DUPLICATE KEY UPDATE name = VALUES(name); 