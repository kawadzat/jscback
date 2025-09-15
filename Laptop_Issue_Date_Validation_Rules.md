# Laptop Issue Date Validation Rules

## Overview
This document explains the validation rules for the `issueDate` field in the laptop/asset management system.

## Validation Rules

### For LAPTOP Assets (`assertType = "LAPTOP"`)
- **Issue date is REQUIRED**
- **Cannot be null**
- **Cannot be zero or invalid date**
- **Must be a valid date after year 2000**

### For Non-LAPTOP Assets (`assertType != "LAPTOP"`)
- **Issue date is OPTIONAL**
- **Can be null**
- **Can be zero or invalid date**
- **No validation restrictions**

## Implementation Details

### Validation Annotation
```java
@LaptopConditionalValidation
public class Laptop {
    // ... fields
}
```

### Validation Logic
The validation is implemented in `LaptopConditionalValidator` and checks:

1. **Asset Type Detection**: Checks if `assertType` equals "LAPTOP" (case-insensitive)
2. **Null Check**: For LAPTOP assets, `issueDate` cannot be null
3. **Zero/Invalid Date Check**: For LAPTOP assets, `issueDate` cannot be:
   - Epoch time (January 1, 1970)
   - Dates before year 2001
   - Invalid date values

### Error Messages
- **Null Issue Date**: "Issue date cannot be null for LAPTOP assets"
- **Zero/Invalid Date**: "Issue date cannot be zero or invalid for LAPTOP assets"

## Usage Examples

### Valid LAPTOP Asset
```json
{
  "assertType": "LAPTOP",
  "issueDate": "2024-01-15",
  "serialNumber": "LAP001",
  "manufacturer": "Dell"
}
```

### Invalid LAPTOP Asset (Missing Issue Date)
```json
{
  "assertType": "LAPTOP",
  "issueDate": null,  // ❌ This will fail validation
  "serialNumber": "LAP001",
  "manufacturer": "Dell"
}
```

### Valid Non-LAPTOP Asset
```json
{
  "assertType": "MONITOR",
  "issueDate": null,  // ✅ This is allowed
  "serialNumber": "MON001",
  "manufacturer": "Samsung"
}
```

### Valid Non-LAPTOP Asset with Zero Date
```json
{
  "assertType": "KEYBOARD",
  "issueDate": "1970-01-01",  // ✅ This is allowed for non-LAPTOP
  "serialNumber": "KB001",
  "manufacturer": "Logitech"
}
```

## API Endpoints Affected

The validation applies to all endpoints that create or update laptop/asset records:

- `POST /laptop` - Create new laptop/asset
- `PUT /laptop/{id}` - Update existing laptop/asset
- `PATCH /laptop/{id}` - Partial update of laptop/asset

## Testing

### Test Cases

1. **LAPTOP with valid issue date** ✅
2. **LAPTOP with null issue date** ❌
3. **LAPTOP with zero date** ❌
4. **LAPTOP with date before 2001** ❌
5. **Non-LAPTOP with null issue date** ✅
6. **Non-LAPTOP with zero date** ✅
7. **Non-LAPTOP with valid date** ✅

### Sample Test Request
```bash
curl -X POST "http://localhost:8080/laptop" \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "assertType": "LAPTOP",
    "issueDate": "2024-01-15",
    "serialNumber": "TEST001",
    "manufacturer": "Test Manufacturer"
  }'
```

## Database Considerations

- The `issue_date` column in the `laptop` table allows NULL values
- Application-level validation ensures data integrity
- Existing records with null issue dates for LAPTOP assets should be updated

## Migration Notes

If you have existing LAPTOP assets with null issue dates, you'll need to:

1. **Identify affected records**:
   ```sql
   SELECT id, assert_type, issue_date 
   FROM laptop 
   WHERE assert_type = 'LAPTOP' AND issue_date IS NULL;
   ```

2. **Update with valid issue dates**:
   ```sql
   UPDATE laptop 
   SET issue_date = '2024-01-01' 
   WHERE assert_type = 'LAPTOP' AND issue_date IS NULL;
   ```

## Frontend Integration

Frontend applications should:

1. **Show issue date as required field** when `assertType` is "LAPTOP"
2. **Show issue date as optional field** for other asset types
3. **Display validation errors** when LAPTOP assets are submitted without issue dates
4. **Allow null/empty issue dates** for non-LAPTOP assets

### Example Frontend Validation
```javascript
function validateIssueDate(assertType, issueDate) {
  if (assertType === 'LAPTOP') {
    if (!issueDate || issueDate === '1970-01-01') {
      return 'Issue date is required for LAPTOP assets';
    }
  }
  return null; // No validation error
}
``` 