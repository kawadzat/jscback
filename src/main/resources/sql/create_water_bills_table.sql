-- Create water_bills table
CREATE TABLE IF NOT EXISTS water_bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_number VARCHAR(25) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    whatsapp_number VARCHAR(20) NOT NULL,
    home_address VARCHAR(500) NOT NULL,
    meter_number VARCHAR(50) NOT NULL,
    billing_period_start DATE NOT NULL,
    billing_period_end DATE NOT NULL,
    previous_reading DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    current_reading DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    consumption DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    rate_per_unit DECIMAL(7,2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    due_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    delivery_method VARCHAR(20) NOT NULL DEFAULT 'WHATSAPP',
    sent_at TIMESTAMP NULL,
    delivery_status VARCHAR(50) NULL,
    delivery_message TEXT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL,
    
    INDEX idx_bill_number (bill_number),
    INDEX idx_customer_phone (customer_phone),
    INDEX idx_whatsapp_number (whatsapp_number),
    INDEX idx_meter_number (meter_number),
    INDEX idx_status (status),
    INDEX idx_delivery_method (delivery_method),
    INDEX idx_due_date (due_date),
    INDEX idx_billing_period (billing_period_start, billing_period_end),
    INDEX idx_created_at (created_at)
);

-- Insert sample data
INSERT INTO water_bills (
    bill_number, customer_name, customer_phone, whatsapp_number, home_address, 
    meter_number, billing_period_start, billing_period_end, previous_reading, 
    current_reading, consumption, rate_per_unit, subtotal, tax, total_amount, 
    due_date, status, delivery_method, notes
) VALUES 
(
    'WB2024120112000012345678', 'John Smith', '+1234567890', '+1234567890',
    '123 Main Street, Apartment 4B, New York, NY 10001',
    'MTR001234', '2024-11-01', '2024-11-30', 1250.50, 1350.75, 100.25, 2.50, 250.63, 12.53, 263.16,
    '2024-12-30', 'PENDING', 'WHATSAPP', 'Regular monthly bill'
),
(
    'WB2024120112000023456789', 'Sarah Johnson', '+1987654321', '+1987654321',
    '456 Oak Avenue, Suite 8, Los Angeles, CA 90210',
    'MTR002345', '2024-11-01', '2024-11-30', 890.25, 945.80, 55.55, 2.50, 138.88, 6.94, 145.82,
    '2024-12-30', 'SENT', 'WHATSAPP', 'Low consumption month'
),
(
    'WB2024120112000034567890', 'Michael Brown', '+1555123456', '+1555123456',
    '789 Pine Street, Unit 12, Chicago, IL 60601',
    'MTR003456', '2024-11-01', '2024-11-30', 2100.00, 2350.50, 250.50, 2.50, 626.25, 31.31, 657.56,
    '2024-12-30', 'OVERDUE', 'POSTAL_MAIL', 'High consumption - possible leak'
),
(
    'WB2024120112000045678901', 'Emily Davis', '+1444567890', '+1444567890',
    '321 Elm Road, House 5, Miami, FL 33101',
    'MTR004567', '2024-11-01', '2024-11-30', 675.30, 720.45, 45.15, 2.50, 112.88, 5.64, 118.52,
    '2024-12-30', 'PAID', 'WHATSAPP', 'Paid on time'
),
(
    'WB2024120112000056789012', 'David Wilson', '+1333567890', '+1333567890',
    '654 Maple Drive, Apartment 15, Seattle, WA 98101',
    'MTR005678', '2024-11-01', '2024-11-30', 1580.75, 1625.90, 45.15, 2.50, 112.88, 5.64, 118.52,
    '2024-12-30', 'PENDING', 'EMAIL', 'New customer'
);

-- Create trigger to calculate consumption, subtotal, and total_amount
DELIMITER //
CREATE TRIGGER calculate_water_bill_amounts
BEFORE INSERT ON water_bills
FOR EACH ROW
BEGIN
    -- Calculate consumption
    SET NEW.consumption = NEW.current_reading - NEW.previous_reading;
    
    -- Calculate subtotal
    SET NEW.subtotal = NEW.consumption * NEW.rate_per_unit;
    
    -- Calculate total amount
    SET NEW.total_amount = NEW.subtotal + NEW.tax;
END//

CREATE TRIGGER update_water_bill_amounts
BEFORE UPDATE ON water_bills
FOR EACH ROW
BEGIN
    -- Calculate consumption
    SET NEW.consumption = NEW.current_reading - NEW.previous_reading;
    
    -- Calculate subtotal
    SET NEW.subtotal = NEW.consumption * NEW.rate_per_unit;
    
    -- Calculate total amount
    SET NEW.total_amount = NEW.subtotal + NEW.tax;
END//
DELIMITER ;

-- Create view for overdue bills
CREATE VIEW overdue_water_bills AS
SELECT * FROM water_bills 
WHERE due_date < CURDATE() 
AND status NOT IN ('PAID', 'CANCELLED');

-- Create view for bills due today
CREATE VIEW water_bills_due_today AS
SELECT * FROM water_bills 
WHERE due_date = CURDATE() 
AND status NOT IN ('PAID', 'CANCELLED');

-- Create view for high consumption bills (consumption > 100 units)
CREATE VIEW high_consumption_water_bills AS
SELECT * FROM water_bills 
WHERE consumption > 100.00;

-- Create view for high amount bills (total_amount > $500)
CREATE VIEW high_amount_water_bills AS
SELECT * FROM water_bills 
WHERE total_amount > 500.00; 