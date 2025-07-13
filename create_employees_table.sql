-- Create Employees Table for HR Module
-- This script creates the employees table with proper structure and constraints

USE securecapita;

-- Drop existing table if it exists
DROP TABLE IF EXISTS employees;

-- Create employees table
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    hire_date DATE NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    salary DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    address TEXT,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    manager_id BIGINT,
    termination_date DATE,
    termination_reason TEXT,
    created_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_date DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NULL,
    last_modified_by VARCHAR(255) NULL,
    
    -- Indexes for better performance
    INDEX idx_employee_id (employee_id),
    INDEX idx_email (email),
    INDEX idx_status (status),
    INDEX idx_department (department),
    INDEX idx_job_title (job_title),
    INDEX idx_manager_id (manager_id),
    INDEX idx_hire_date (hire_date),
    INDEX idx_name (first_name, last_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, date_of_birth, hire_date, job_title, department, salary, status) VALUES
('EMP001', 'John', 'Doe', 'john.doe@company.com', '+1234567890', '1990-05-15', '2023-01-15', 'Software Engineer', 'IT', 75000.00, 'ACTIVE'),
('EMP002', 'Jane', 'Smith', 'jane.smith@company.com', '+1234567891', '1988-08-22', '2023-02-01', 'HR Manager', 'Human Resources', 85000.00, 'ACTIVE'),
('EMP003', 'Mike', 'Johnson', 'mike.johnson@company.com', '+1234567892', '1992-03-10', '2023-03-10', 'Sales Representative', 'Sales', 60000.00, 'ACTIVE'),
('EMP004', 'Sarah', 'Williams', 'sarah.williams@company.com', '+1234567893', '1985-12-05', '2023-01-20', 'Marketing Specialist', 'Marketing', 65000.00, 'ACTIVE'),
('EMP005', 'David', 'Brown', 'david.brown@company.com', '+1234567894', '1991-07-18', '2023-04-01', 'Data Analyst', 'IT', 70000.00, 'ACTIVE');

-- Verify the table structure
DESCRIBE employees;

-- Show sample data
SELECT * FROM employees; 