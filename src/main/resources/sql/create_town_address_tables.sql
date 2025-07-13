-- Create town_addresses table
CREATE TABLE IF NOT EXISTS town_addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    town_code VARCHAR(20) NOT NULL UNIQUE,
    town_name VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    district VARCHAR(100) NOT NULL,
    region VARCHAR(100) NOT NULL,
    main_office_address VARCHAR(500) NOT NULL,
    main_office_phone VARCHAR(20) NOT NULL,
    main_office_whatsapp VARCHAR(20) NOT NULL,
    emergency_phone VARCHAR(20) NOT NULL,
    customer_service_phone VARCHAR(20) NOT NULL,
    customer_service_whatsapp VARCHAR(20) NOT NULL,
    billing_office_phone VARCHAR(20) NOT NULL,
    billing_office_whatsapp VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    website VARCHAR(200) NOT NULL,
    timezone VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    language VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    description TEXT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL,
    
    INDEX idx_town_code (town_code),
    INDEX idx_town_name (town_name),
    INDEX idx_state (state),
    INDEX idx_country (country),
    INDEX idx_postal_code (postal_code),
    INDEX idx_district (district),
    INDEX idx_region (region),
    INDEX idx_is_active (is_active),
    INDEX idx_currency (currency),
    INDEX idx_language (language),
    INDEX idx_timezone (timezone),
    INDEX idx_main_office_phone (main_office_phone),
    INDEX idx_billing_office_phone (billing_office_phone),
    INDEX idx_created_at (created_at)
);

-- Create customer_addresses table
CREATE TABLE IF NOT EXISTS customer_addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    customer_whatsapp VARCHAR(20) NOT NULL,
    customer_email VARCHAR(100) NOT NULL,
    street_address VARCHAR(200) NOT NULL,
    apartment_unit VARCHAR(50) NOT NULL,
    neighborhood VARCHAR(100) NOT NULL,
    landmark VARCHAR(200) NULL,
    address_type VARCHAR(20) NOT NULL, -- HOME, BUSINESS, BILLING, SHIPPING
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT NULL,
    town_address_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL,
    
    FOREIGN KEY (town_address_id) REFERENCES town_addresses(id) ON DELETE CASCADE,
    
    INDEX idx_customer_name (customer_name),
    INDEX idx_customer_phone (customer_phone),
    INDEX idx_customer_whatsapp (customer_whatsapp),
    INDEX idx_customer_email (customer_email),
    INDEX idx_address_type (address_type),
    INDEX idx_is_primary (is_primary),
    INDEX idx_is_active (is_active),
    INDEX idx_neighborhood (neighborhood),
    INDEX idx_landmark (landmark),
    INDEX idx_street_address (street_address),
    INDEX idx_apartment_unit (apartment_unit),
    INDEX idx_town_address_id (town_address_id),
    INDEX idx_created_at (created_at)
);

-- Insert sample town addresses
INSERT INTO town_addresses (
    town_code, town_name, state, country, postal_code, district, region,
    main_office_address, main_office_phone, main_office_whatsapp, emergency_phone,
    customer_service_phone, customer_service_whatsapp, billing_office_phone, billing_office_whatsapp,
    email, website, timezone, currency, language, description
) VALUES 
(
    'NYC001', 'New York City', 'New York', 'USA', '10001', 'Manhattan', 'Northeast',
    '123 Water Street, Manhattan, NY 10001', '+12125551234', '+12125551234', '+12125559999',
    '+12125551235', '+12125551235', '+12125551236', '+12125551236',
    'water@nyc.gov', 'https://www.nyc.gov/water', 'America/New_York', 'USD', 'en',
    'New York City Water Department - Main Office'
),
(
    'LAX001', 'Los Angeles', 'California', 'USA', '90210', 'Beverly Hills', 'West',
    '456 Water Avenue, Beverly Hills, CA 90210', '+13105551234', '+13105551234', '+13105559999',
    '+13105551235', '+13105551235', '+13105551236', '+13105551236',
    'water@la.gov', 'https://www.lacity.org/water', 'America/Los_Angeles', 'USD', 'en',
    'Los Angeles Water Department - Main Office'
),
(
    'CHI001', 'Chicago', 'Illinois', 'USA', '60601', 'Downtown', 'Midwest',
    '789 Water Boulevard, Downtown, IL 60601', '+17735551234', '+17735551234', '+17735559999',
    '+17735551235', '+17735551235', '+17735551236', '+17735551236',
    'water@chicago.gov', 'https://www.chicago.gov/water', 'America/Chicago', 'USD', 'en',
    'Chicago Water Department - Main Office'
),
(
    'MIA001', 'Miami', 'Florida', 'USA', '33101', 'Downtown', 'Southeast',
    '321 Water Road, Downtown, FL 33101', '+13055551234', '+13055551234', '+13055559999',
    '+13055551235', '+13055551235', '+13055551236', '+13055551236',
    'water@miami.gov', 'https://www.miamigov.com/water', 'America/New_York', 'USD', 'en',
    'Miami Water Department - Main Office'
),
(
    'SEA001', 'Seattle', 'Washington', 'USA', '98101', 'Downtown', 'Northwest',
    '654 Water Drive, Downtown, WA 98101', '+12065551234', '+12065551234', '+12065559999',
    '+12065551235', '+12065551235', '+12065551236', '+12065551236',
    'water@seattle.gov', 'https://www.seattle.gov/water', 'America/Los_Angeles', 'USD', 'en',
    'Seattle Water Department - Main Office'
);

-- Insert sample customer addresses
INSERT INTO customer_addresses (
    customer_name, customer_phone, customer_whatsapp, customer_email,
    street_address, apartment_unit, neighborhood, landmark, address_type, is_primary,
    town_address_id
) VALUES 
(
    'John Smith', '+1234567890', '+1234567890', 'john.smith@email.com',
    '123 Main Street', 'Apt 4B', 'Downtown', 'Near Central Park', 'HOME', TRUE,
    (SELECT id FROM town_addresses WHERE town_code = 'NYC001')
),
(
    'Sarah Johnson', '+1987654321', '+1987654321', 'sarah.johnson@email.com',
    '456 Oak Avenue', 'Suite 8', 'Beverly Hills', 'Near Rodeo Drive', 'HOME', TRUE,
    (SELECT id FROM town_addresses WHERE town_code = 'LAX001')
),
(
    'Michael Brown', '+1555123456', '+1555123456', 'michael.brown@email.com',
    '789 Pine Street', 'Unit 12', 'Downtown', 'Near Millennium Park', 'HOME', TRUE,
    (SELECT id FROM town_addresses WHERE town_code = 'CHI001')
),
(
    'Emily Davis', '+1444567890', '+1444567890', 'emily.davis@email.com',
    '321 Elm Road', 'House 5', 'Downtown', 'Near Bayfront Park', 'HOME', TRUE,
    (SELECT id FROM town_addresses WHERE town_code = 'MIA001')
),
(
    'David Wilson', '+1333567890', '+1333567890', 'david.wilson@email.com',
    '654 Maple Drive', 'Apt 15', 'Downtown', 'Near Pike Place Market', 'HOME', TRUE,
    (SELECT id FROM town_addresses WHERE town_code = 'SEA001')
),
(
    'John Smith', '+1234567890', '+1234567890', 'john.smith@email.com',
    '456 Business Street', 'Suite 100', 'Midtown', 'Near Empire State Building', 'BUSINESS', FALSE,
    (SELECT id FROM town_addresses WHERE town_code = 'NYC001')
),
(
    'Sarah Johnson', '+1987654321', '+1987654321', 'sarah.johnson@email.com',
    '789 Billing Street', 'Floor 3', 'Financial District', 'Near City Hall', 'BILLING', FALSE,
    (SELECT id FROM town_addresses WHERE town_code = 'LAX001')
);

-- Update water_bills table to include town_address_id and customer_address_id
ALTER TABLE water_bills ADD COLUMN town_address_id BIGINT NULL;
ALTER TABLE water_bills ADD COLUMN customer_address_id BIGINT NULL;
ALTER TABLE water_bills ADD FOREIGN KEY (town_address_id) REFERENCES town_addresses(id) ON DELETE SET NULL;
ALTER TABLE water_bills ADD FOREIGN KEY (customer_address_id) REFERENCES customer_addresses(id) ON DELETE SET NULL;
ALTER TABLE water_bills ADD INDEX idx_town_address_id (town_address_id);
ALTER TABLE water_bills ADD INDEX idx_customer_address_id (customer_address_id);

-- Update existing water bills to link with town addresses and customer addresses
UPDATE water_bills SET 
    town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'NYC001'),
    customer_address_id = (SELECT id FROM customer_addresses WHERE customer_name = 'John Smith' AND address_type = 'HOME' AND town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'NYC001'))
WHERE customer_name = 'John Smith';

UPDATE water_bills SET 
    town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'LAX001'),
    customer_address_id = (SELECT id FROM customer_addresses WHERE customer_name = 'Sarah Johnson' AND address_type = 'HOME' AND town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'LAX001'))
WHERE customer_name = 'Sarah Johnson';

UPDATE water_bills SET 
    town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'CHI001'),
    customer_address_id = (SELECT id FROM customer_addresses WHERE customer_name = 'Michael Brown' AND address_type = 'HOME' AND town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'CHI001'))
WHERE customer_name = 'Michael Brown';

UPDATE water_bills SET 
    town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'MIA001'),
    customer_address_id = (SELECT id FROM customer_addresses WHERE customer_name = 'Emily Davis' AND address_type = 'HOME' AND town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'MIA001'))
WHERE customer_name = 'Emily Davis';

UPDATE water_bills SET 
    town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'SEA001'),
    customer_address_id = (SELECT id FROM customer_addresses WHERE customer_name = 'David Wilson' AND address_type = 'HOME' AND town_address_id = (SELECT id FROM town_addresses WHERE town_code = 'SEA001'))
WHERE customer_name = 'David Wilson';

-- Create views for common queries
CREATE VIEW active_town_addresses AS
SELECT * FROM town_addresses WHERE is_active = TRUE;

CREATE VIEW active_customer_addresses AS
SELECT * FROM customer_addresses WHERE is_active = TRUE;

CREATE VIEW primary_customer_addresses AS
SELECT * FROM customer_addresses WHERE is_primary = TRUE AND is_active = TRUE;

CREATE VIEW customer_addresses_with_town AS
SELECT 
    ca.*,
    ta.town_name,
    ta.state,
    ta.country,
    ta.postal_code,
    ta.district,
    ta.region,
    ta.main_office_phone,
    ta.billing_office_phone,
    ta.customer_service_phone
FROM customer_addresses ca
JOIN town_addresses ta ON ca.town_address_id = ta.id;

CREATE VIEW water_bills_with_town_and_customer AS
SELECT 
    wb.*,
    ta.town_name,
    ta.state,
    ta.country,
    ta.billing_office_phone,
    ta.billing_office_whatsapp,
    ta.customer_service_phone,
    ta.customer_service_whatsapp,
    ca.customer_name,
    ca.customer_phone,
    ca.customer_whatsapp,
    ca.customer_email,
    ca.street_address,
    ca.apartment_unit,
    ca.neighborhood,
    ca.landmark,
    ca.address_type
FROM water_bills wb
LEFT JOIN town_addresses ta ON wb.town_address_id = ta.id
LEFT JOIN customer_addresses ca ON wb.customer_address_id = ca.id;

-- Create indexes for better performance
CREATE INDEX idx_customer_addresses_town_name ON customer_addresses(customer_name, town_address_id);
CREATE INDEX idx_customer_addresses_phone_type ON customer_addresses(customer_phone, address_type);
CREATE INDEX idx_customer_addresses_whatsapp_type ON customer_addresses(customer_whatsapp, address_type);
CREATE INDEX idx_town_addresses_state_country ON town_addresses(state, country);
CREATE INDEX idx_town_addresses_currency_language ON town_addresses(currency, language); 