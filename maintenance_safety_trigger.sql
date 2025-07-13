-- Maintenance Safety Trigger
-- This trigger ensures the description field is never null

DELIMITER $$

-- Drop existing triggers
DROP TRIGGER IF EXISTS maintenance_before_insert$$
DROP TRIGGER IF EXISTS maintenance_before_update$$

-- Create trigger for INSERT operations
CREATE TRIGGER maintenance_before_insert
BEFORE INSERT ON maintenance
FOR EACH ROW
BEGIN
    -- Set default description if null or empty
    IF NEW.description IS NULL OR TRIM(NEW.description) = '' THEN
        IF NEW.laptop_id IS NOT NULL THEN
            SET NEW.description = CONCAT('Scheduled maintenance for laptop ', NEW.laptop_id);
        ELSE
            SET NEW.description = 'Scheduled maintenance task';
        END IF;
    END IF;
    
    -- Set other defaults if null
    IF NEW.maintenance_type IS NULL THEN
        SET NEW.maintenance_type = 'PREVENTIVE';
    END IF;
    
    IF NEW.scheduled_date IS NULL THEN
        SET NEW.scheduled_date = NOW() + INTERVAL 1 DAY;
    END IF;
    
    IF NEW.status IS NULL THEN
        SET NEW.status = 'SCHEDULED';
    END IF;
    
    IF NEW.priority IS NULL THEN
        SET NEW.priority = 'MEDIUM';
    END IF;
END$$

-- Create trigger for UPDATE operations
CREATE TRIGGER maintenance_before_update
BEFORE UPDATE ON maintenance
FOR EACH ROW
BEGIN
    -- Set default description if null or empty
    IF NEW.description IS NULL OR TRIM(NEW.description) = '' THEN
        IF NEW.laptop_id IS NOT NULL THEN
            SET NEW.description = CONCAT('Scheduled maintenance for laptop ', NEW.laptop_id);
        ELSE
            SET NEW.description = 'Scheduled maintenance task';
        END IF;
    END IF;
    
    -- Set other defaults if null
    IF NEW.maintenance_type IS NULL THEN
        SET NEW.maintenance_type = 'PREVENTIVE';
    END IF;
    
    IF NEW.scheduled_date IS NULL THEN
        SET NEW.scheduled_date = NOW() + INTERVAL 1 DAY;
    END IF;
    
    IF NEW.status IS NULL THEN
        SET NEW.status = 'SCHEDULED';
    END IF;
    
    IF NEW.priority IS NULL THEN
        SET NEW.priority = 'MEDIUM';
    END IF;
END$$

DELIMITER ;

-- Verify triggers were created
SHOW TRIGGERS LIKE 'maintenance';

-- Test the trigger with null description
INSERT INTO maintenance (laptop_id, description) VALUES (1, NULL);

-- Verify the trigger worked
SELECT 
    id,
    description,
    laptop_id
FROM maintenance 
WHERE id = LAST_INSERT_ID(); 