-- Make all columns nullable for non-laptop assets
-- This allows other asset types (desktop, tablet, etc.) to be stored with minimal required fields

-- Update all required columns to be nullable
ALTER TABLE laptop MODIFY COLUMN purchase_date TIMESTAMP NULL;
ALTER TABLE laptop MODIFY COLUMN manufacturer VARCHAR(100) NULL;
ALTER TABLE laptop MODIFY COLUMN assert_type VARCHAR(100) NULL;
ALTER TABLE laptop MODIFY COLUMN serial_number VARCHAR(50) NULL;
ALTER TABLE laptop MODIFY COLUMN ram INT NULL;
ALTER TABLE laptop MODIFY COLUMN processor INT NULL;
ALTER TABLE laptop MODIFY COLUMN issue_date TIMESTAMP NULL;
ALTER TABLE laptop MODIFY COLUMN status VARCHAR(50) NULL;
ALTER TABLE laptop MODIFY COLUMN issued_to VARCHAR(100) NULL;
ALTER TABLE laptop MODIFY COLUMN station VARCHAR(100) NULL;
ALTER TABLE laptop MODIFY COLUMN department VARCHAR(100) NULL;
ALTER TABLE laptop MODIFY COLUMN designation VARCHAR(100) NULL;
ALTER TABLE laptop MODIFY COLUMN email VARCHAR(255) NULL;
ALTER TABLE laptop MODIFY COLUMN replacement_date TIMESTAMP NULL;

-- Add comments to explain the change
ALTER TABLE laptop MODIFY COLUMN purchase_date TIMESTAMP NULL COMMENT 'Purchase date - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN manufacturer VARCHAR(100) NULL COMMENT 'Manufacturer - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN assert_type VARCHAR(100) NULL COMMENT 'Asset type - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN serial_number VARCHAR(50) NULL COMMENT 'Serial number - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN ram INT NULL COMMENT 'RAM in GB - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN processor INT NULL COMMENT 'Processor cores - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN issue_date TIMESTAMP NULL COMMENT 'Issue date - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN status VARCHAR(50) NULL COMMENT 'Status - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN issued_to VARCHAR(100) NULL COMMENT 'Issued to - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN station VARCHAR(100) NULL COMMENT 'Station - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN department VARCHAR(100) NULL COMMENT 'Department - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN designation VARCHAR(100) NULL COMMENT 'Designation - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN email VARCHAR(255) NULL COMMENT 'Email - Required for LAPTOP assets, optional for others';
ALTER TABLE laptop MODIFY COLUMN replacement_date TIMESTAMP NULL COMMENT 'Replacement date - Required for LAPTOP assets, optional for others';

-- Update existing records to set fields to NULL for non-laptop assets (optional)
-- UPDATE laptop SET 
--     purchase_date = NULL,
--     manufacturer = NULL,
--     serial_number = NULL,
--     ram = NULL,
--     processor = NULL,
--     issue_date = NULL,
--     status = NULL,
--     issued_to = NULL,
--     station = NULL,
--     department = NULL,
--     designation = NULL,
--     email = NULL,
--     replacement_date = NULL
-- WHERE assert_type != 'LAPTOP' AND assert_type IS NOT NULL; 