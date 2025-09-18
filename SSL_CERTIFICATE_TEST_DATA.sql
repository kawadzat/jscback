-- SSL Certificate Test Data for Notification System Testing
-- This file contains sample SSL certificates with various expiry dates for testing

-- Insert test SSL certificates with different expiry scenarios

-- Certificate expiring in exactly 2 months
INSERT INTO sslcertificate (
    purchase_date, 
    category, 
    plan, 
    validity, 
    vendor, 
    supplier, 
    expiry_date, 
    domain_name, 
    certificate_serial_number, 
    notes
) VALUES (
    '2024-01-15', 
    'Web Server', 
    'Standard SSL', 
    '1 Year', 
    'DigiCert', 
    'DigiCert Inc.', 
    DATE_ADD(CURDATE(), INTERVAL 2 MONTH), 
    'example1.jsc.com', 
    'SSL-001-2024', 
    'Test certificate for notification system - expires in 2 months'
);

-- Certificate expiring in 2 months + 3 days
INSERT INTO sslcertificate (
    purchase_date, 
    category, 
    plan, 
    validity, 
    vendor, 
    supplier, 
    expiry_date, 
    domain_name, 
    certificate_serial_number, 
    notes
) VALUES (
    '2024-01-15', 
    'API Server', 
    'Wildcard SSL', 
    '1 Year', 
    'Let\'s Encrypt', 
    'Let\'s Encrypt', 
    DATE_ADD(CURDATE(), INTERVAL 2 MONTH + INTERVAL 3 DAY), 
    'example2.jsc.com', 
    'SSL-002-2024', 
    'Test certificate for notification system - expires in 2 months + 3 days'
);

-- Certificate expiring in 2 months - 5 days
INSERT INTO sslcertificate (
    purchase_date, 
    category, 
    plan, 
    validity, 
    vendor, 
    supplier, 
    expiry_date, 
    domain_name, 
    certificate_serial_number, 
    notes
) VALUES (
    '2024-01-15', 
    'Database Server', 
    'Extended Validation SSL', 
    '1 Year', 
    'GlobalSign', 
    'GlobalSign Ltd.', 
    DATE_ADD(CURDATE(), INTERVAL 2 MONTH - INTERVAL 5 DAY), 
    'example3.jsc.com', 
    'SSL-003-2024', 
    'Test certificate for notification system - expires in 2 months - 5 days'
);

-- Display the test data for verification
SELECT 
    id,
    domain_name,
    certificate_serial_number,
    vendor,
    expiry_date,
    DATEDIFF(expiry_date, CURDATE()) as days_until_expiry,
    CASE 
        WHEN expiry_date IS NULL THEN 'No expiry date'
        WHEN DATEDIFF(expiry_date, CURDATE()) BETWEEN 50 AND 80 THEN 'Should trigger notification (2 months ±7 days)'
        ELSE 'Should NOT trigger notification'
    END as notification_status
FROM sslcertificate 
WHERE notes LIKE '%Test certificate for notification system%'
ORDER BY expiry_date;


















