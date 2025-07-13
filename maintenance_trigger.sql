-- Maintenance Table Trigger
-- This trigger ensures the description field is never null or empty

DELIMITER $$

-- Drop trigger if it exists
DROP TRIGGER IF EXISTS maintenance_before_insert$$

-- Create trigger for INSERT operations
CREATE TRIGGER maintenance_before_insert
BEFORE INSERT ON maintenance
FOR EACH ROW
BEGIN
    -- Ensure description is never null or empty
    IF NEW.description IS NULL OR TRIM(NEW.description) = '' THEN
        SET NEW.description = CONCAT('Scheduled maintenance for laptop ', NEW.laptop_id);
    END IF;
    
    -- Ensure maintenance_type is never null
    IF NEW.maintenance_type IS NULL THEN
        SET NEW.maintenance_type = 'PREVENTIVE';
    END IF;
    
    -- Ensure scheduled_date is never null
    IF NEW.scheduled_date IS NULL THEN
        SET NEW.scheduled_date = NOW() + INTERVAL 1 DAY;
    END IF;
    
    -- Ensure status is never null
    IF NEW.status IS NULL THEN
        SET NEW.status = 'SCHEDULED';
    END IF;
    
    -- Ensure priority is never null
    IF NEW.priority IS NULL THEN
        SET NEW.priority = 'MEDIUM';
    END IF;
END$$

-- Drop trigger if it exists
DROP TRIGGER IF EXISTS maintenance_before_update$$

-- Create trigger for UPDATE operations
CREATE TRIGGER maintenance_before_update
BEFORE UPDATE ON maintenance
FOR EACH ROW
BEGIN
    -- Ensure description is never null or empty
    IF NEW.description IS NULL OR TRIM(NEW.description) = '' THEN
        SET NEW.description = CONCAT('Scheduled maintenance for laptop ', NEW.laptop_id);
    END IF;
    
    -- Ensure maintenance_type is never null
    IF NEW.maintenance_type IS NULL THEN
        SET NEW.maintenance_type = 'PREVENTIVE';
    END IF;
    
    -- Ensure scheduled_date is never null
    IF NEW.scheduled_date IS NULL THEN
        SET NEW.scheduled_date = NOW() + INTERVAL 1 DAY;
    END IF;
    
    -- Ensure status is never null
    IF NEW.status IS NULL THEN
        SET NEW.status = 'SCHEDULED';
    END IF;
    
    -- Ensure priority is never null
    IF NEW.priority IS NULL THEN
        SET NEW.priority = 'MEDIUM';
    END IF;
END$$

DELIMITER ;

-- Verify triggers were created
SHOW TRIGGERS LIKE 'maintenance'; 