# Antivirus API Update Endpoints Documentation

This document provides comprehensive information about all antivirus update endpoints available in the JSC Backend API.

## Base URL
```
/antivirus
```

## Authentication
All endpoints require JWT authentication. Include the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Update Endpoints

### 1. Basic Update Antivirus
**PUT** `/antivirus/id/{antivirusId}`

Updates all fields of an antivirus record.

**Request Body:**
```json
{
  "name": "Updated Antivirus Name",
  "key": "NEW-KEY-12345",
  "renewTimeInterval": 365,
  "version": "2024.1.0",
  "vendor": "Updated Vendor",
  "status": "ACTIVE",
  "isInstalled": true,
  "licenseExpirationDate": "2025-12-31T23:59:59",
  "lastScanDate": "2024-01-15T10:30:00"
}
```

**Response:** Returns the updated Antivirus object.

### 2. Update Antivirus with Duplicate Check
**PUT** `/antivirus/id/{antivirusId}/with-duplicate-check`

Updates antivirus with validation to prevent duplicate keys.

**Request Body:** Same as basic update.

**Response:** Returns the updated Antivirus object or error if key already exists.

### 3. Update Antivirus on Specific Laptop
**PUT** `/antivirus/laptop/{laptopId}/antivirus/{antivirusId}`

Updates antivirus that belongs to a specific laptop.

**Request Body:** Same as basic update.

**Response:** Returns the updated Antivirus object.

### 4. Partial Update Antivirus
**PATCH** `/antivirus/id/{antivirusId}`

Updates only specific fields of an antivirus record.

**Request Body:**
```json
{
  "name": "New Name",
  "version": "2024.2.0",
  "status": "INACTIVE"
}
```

**Available fields for partial update:**
- `name` (String)
- `key` (String) - with duplicate check
- `renewTimeInterval` (Integer)
- `version` (String)
- `vendor` (String)
- `status` (String) - "ACTIVE", "INACTIVE", "EXPIRED"
- `isInstalled` (Boolean)
- `licenseExpirationDate` (String) - ISO format
- `lastScanDate` (String) - ISO format

**Response:** Returns the updated Antivirus object.

### 5. Update Antivirus Status Only
**PATCH** `/antivirus/id/{antivirusId}/status`

Updates only the status field.

**Request Body:**
```json
{
  "status": "ACTIVE"
}
```

**Valid status values:** "ACTIVE", "INACTIVE", "EXPIRED"

**Response:** Returns the updated Antivirus object.

### 6. Update Antivirus Installation Status
**PATCH** `/antivirus/id/{antivirusId}/installation`

Updates only the installation status.

**Request Body:**
```json
{
  "isInstalled": true
}
```

**Response:** Returns the updated Antivirus object.

### 7. Update License Expiration Date
**PATCH** `/antivirus/id/{antivirusId}/license-expiration`

Updates only the license expiration date.

**Request Body:**
```json
{
  "licenseExpirationDate": "2025-12-31T23:59:59"
}
```

**Response:** Returns the updated Antivirus object.

### 8. Update Last Scan Date
**PATCH** `/antivirus/id/{antivirusId}/last-scan`

Updates only the last scan date.

**Request Body:**
```json
{
  "lastScanDate": "2024-01-15T10:30:00"
}
```

**Response:** Returns the updated Antivirus object.

## Delete Endpoints

### 9. Delete Antivirus
**DELETE** `/antivirus/id/{antivirusId}`

Permanently deletes an antivirus record.

**Response:**
```json
{
  "message": "Antivirus deleted successfully",
  "antivirusId": "123"
}
```

### 10. Remove Antivirus from Laptop
**DELETE** `/antivirus/laptop/{laptopId}/antivirus/{antivirusId}`

Removes antivirus from a specific laptop.

**Response:**
```json
{
  "message": "Antivirus removed from laptop successfully",
  "laptopId": "456",
  "antivirusId": "123"
}
```

## Search and Filter Endpoints

### 11. Search by Name
**GET** `/antivirus/search/name/{name}`

Searches antivirus by name (case-insensitive partial match).

### 12. Filter by Vendor
**GET** `/antivirus/vendor/{vendor}`

Gets all antivirus from a specific vendor.

### 13. Filter by Status
**GET** `/antivirus/status/{status}`

Gets all antivirus with a specific status.

### 14. Get Installed Antivirus
**GET** `/antivirus/installed`

Gets all installed antivirus only.

### 15. Get Expiring Licenses
**GET** `/antivirus/expiring`

Gets antivirus with licenses expiring in the next 30 days.

### 16. Get Expiring Licenses (Custom Days)
**GET** `/antivirus/expiring/{days}`

Gets antivirus with licenses expiring in the specified number of days.

### 17. Get Antivirus Statistics
**GET** `/antivirus/statistics`

Returns comprehensive statistics about antivirus records.

**Response:**
```json
{
  "totalAntivirus": 150,
  "installedCount": 120,
  "activeCount": 100,
  "expiredCount": 10,
  "expiringSoonCount": 5
}
```

## Error Handling

All endpoints return appropriate HTTP status codes:

- **200 OK** - Success
- **201 Created** - Resource created successfully
- **400 Bad Request** - Invalid input data
- **401 Unauthorized** - Missing or invalid authentication
- **404 Not Found** - Resource not found
- **409 Conflict** - Duplicate key or constraint violation

## Example Usage

### Update Antivirus Name and Version
```bash
curl -X PATCH "http://localhost:8080/antivirus/id/123" \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Antivirus Pro",
    "version": "2024.2.1"
  }'
```

### Update License Expiration
```bash
curl -X PATCH "http://localhost:8080/antivirus/id/123/license-expiration" \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "licenseExpirationDate": "2025-06-30T23:59:59"
  }'
```

### Get Expiring Licenses
```bash
curl -X GET "http://localhost:8080/antivirus/expiring/60" \
  -H "Authorization: Bearer <your-token>"
```

## Notes

1. **Date Format:** All dates should be in ISO 8601 format (YYYY-MM-DDTHH:mm:ss)
2. **Key Uniqueness:** Antivirus keys must be unique across the system
3. **Validation:** All endpoints include proper validation and error handling
4. **Audit Trail:** Updates are tracked in the system for audit purposes
5. **Cascade Effects:** Deleting an antivirus will remove all associated relationships

## Frontend Integration

For frontend applications, consider using the PATCH endpoints for better user experience as they allow partial updates without requiring all fields to be sent in the request. 