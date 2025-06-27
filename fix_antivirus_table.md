# Fix Antivirus Table Issue

## Problem
The error "Table 'securecapita.antivirus' doesn't exist" indicates that the JPA auto-generation didn't create the antivirus table.

## Solution Options

### Option 1: Run SQL Script (Recommended)
1. Connect to your MySQL database:
   ```bash
   mysql -u root -p -h localhost -P 3307 securecapita
   ```

2. Run the SQL script:
   ```sql
   source create_antivirus_table.sql
   ```

### Option 2: Force JPA to Create Tables
1. Temporarily change the JPA configuration in `application.yml`:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: create-drop  # This will recreate all tables
   ```

2. Start the application once to create tables
3. Change back to:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: update
   ```

### Option 3: Use JPA Schema Generation
1. Add this to your `application.yml`:
   ```yaml
   spring:
     jpa:
       hibernate:
         ddl-auto: validate
       generate-ddl: true
       show-sql: true
   ```

2. Create a schema.sql file in `src/main/resources/` with the table definition

## Verify the Fix

After creating the table, test with:
```bash
# Check if table exists
mysql -u root -p -h localhost -P 3307 securecapita -e "DESCRIBE antivirus;"

# Test the API
curl -X POST http://localhost:8080/api/v1/antivirus/laptop/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Norton Antivirus",
    "key": "NORTON-2024-ABC123",
    "renewTimeInterval": 365,
    "version": "2024.1.0",
    "vendor": "Norton",
    "licenseExpirationDate": "2025-12-31T23:59:59",
    "lastScanDate": "2024-01-15T10:30:00"
  }'
```

## Table Structure
The antivirus table includes:
- `id`: Primary key
- `name`: Antivirus name
- `key`: Unique license key
- `renew_time_interval`: Days until renewal
- `version`: Software version
- `vendor`: Vendor name
- `status`: ACTIVE, INACTIVE, EXPIRED
- `is_installed`: Boolean flag
- `license_expiration_date`: When license expires
- `last_scan_date`: Last scan timestamp
- `laptop_id`: Foreign key to laptop table
- Audit fields: created_date, last_modified_date, created_by, last_modified_by

## Indexes
The script creates indexes for:
- laptop_id (for relationship queries)
- key (for unique lookups)
- vendor (for vendor searches)
- status (for status filtering)
- license_expiration_date (for expiring license queries)
- last_scan_date (for scan history queries) 