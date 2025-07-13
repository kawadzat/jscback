# Automatic Acknowledgment Feature

## Overview

The automatic acknowledgment feature ensures that whenever a laptop is issued (status changed to `ISSUED`), a pending acknowledgment is automatically created. This provides a seamless workflow where station admins can review and manually acknowledge laptop issuances.

## How It Works

### 1. Automatic Acknowledgment Creation

When any of the following actions occur, an automatic acknowledgment is created:

- **Status Change to ISSUED**: When `changeLaptopStatus()` is called with `LaptopStatus.ISSUED`
- **Assignment Update**: When `updateLaptopAssignment()` is called for a laptop that wasn't previously issued
- **Full Update to ISSUED**: When `updateLaptop()` is called with status `ISSUED`

### 2. Status Flow

```
AVAILABLE → PENDING_ACKNOWLEDGMENT → ISSUED
```

1. **PENDING_ACKNOWLEDGMENT**: Automatically set when laptop is being issued
2. **ISSUED**: Manually set by station admin after acknowledgment

### 3. Automatic Acknowledgment Details

The system creates an acknowledgment record with:
- **Signature Type**: `AUTOMATIC`
- **Notes**: "Automatic acknowledgment created when laptop was issued"
- **IP Address**: `SYSTEM`
- **User Agent**: `SYSTEM_AUTO_ACKNOWLEDGMENT`
- **Certificate Info**: "Automatic system acknowledgment"
- **Signature**: `null` (no signature for automatic acknowledgment)
- **Signature Hash**: `null` (no signature hash for automatic acknowledgment)

## API Endpoints

### 1. Status Change (Automatic Acknowledgment)

**Endpoint**: `PUT /laptop/{id}/status`

**Request Body**:
```json
"ISSUED"
```

**Response**:
```json
{
  "message": "Laptop Status Updated Successfully",
  "data": {
    "id": 1,
    "status": "PENDING_ACKNOWLEDGMENT",
    "issuedTo": "John Doe",
    "station": "IT Department",
    // ... other fields
  }
}
```

### 2. Assignment Update (Automatic Acknowledgment)

**Endpoint**: `PUT /laptop/{id}/assignment`

**Request Body**:
```json
{
  "issuedTo": "John Doe",
  "station": "IT Department",
  "department": "Information Technology",
  "designation": "Software Developer",
  "email": "john.doe@company.com"
}
```

**Response**:
```json
{
  "message": "Laptop Assignment Updated Successfully",
  "data": {
    "id": 1,
    "status": "PENDING_ACKNOWLEDGMENT",
    "issuedTo": "John Doe",
    "station": "IT Department",
    // ... other fields
  }
}
```

### 3. Manual Acknowledgment

**Endpoint**: `POST /laptop/{id}/manual-acknowledge`

**Request Body** (optional):
```json
"Laptop has been physically received and verified by station admin"
```

**Response**:
```json
{
  "message": "Laptop manually acknowledged and status changed to ISSUED",
  "data": {
    "id": 1,
    "status": "ISSUED",
    "issuedTo": "John Doe",
    "station": "IT Department",
    // ... other fields
  }
}
```

### 4. Get Pending Acknowledgments

**Endpoint**: `GET /laptop/pending-acknowledgment/{station}`

**Response**:
```json
[
  {
    "id": 1,
    "status": "PENDING_ACKNOWLEDGMENT",
    "issuedTo": "John Doe",
    "station": "IT Department",
    // ... other fields
  }
]
```

### 5. Get Acknowledgment History

**Endpoint**: `GET /laptop/{id}/acknowledgment`

**Response**:
```json
{
  "id": 1,
  "laptopId": 1,
  "acknowledgedBy": {
    "id": 1,
    "firstName": "Admin",
    "lastName": "User"
  },
  "acknowledgmentDate": "2024-01-20T10:30:00",
  "notes": "Automatic acknowledgment created when laptop was issued",
  "signatureType": "AUTOMATIC",
  "signatureTimestamp": "2024-01-20T10:30:00",
  "ipAddress": "SYSTEM",
  "userAgent": "SYSTEM_AUTO_ACKNOWLEDGMENT",
  "certificateInfo": "Automatic system acknowledgment",
  "laptopSerialNumber": "LAP-2024-001",
  "laptopIssuedTo": "John Doe",
  "laptopStation": "IT Department"
}
```

## Business Logic

### Automatic Acknowledgment Creation

```java
private void createAutomaticAcknowledgment(UserDTO currentUser, Laptop laptop) {
    try {
        User user = userRepository1.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        LaptopAcknowledgment acknowledgment = LaptopAcknowledgment.builder()
                .laptop(laptop)
                .acknowledgedBy(user)
                .notes("Automatic acknowledgment created when laptop was issued")
                .signature(null)
                .signatureType("AUTOMATIC")
                .signatureTimestamp(LocalDateTime.now())
                .ipAddress("SYSTEM")
                .userAgent("SYSTEM_AUTO_ACKNOWLEDGMENT")
                .certificateInfo("Automatic system acknowledgment")
                .signatureHash(null)
                .build();
        
        laptopAcknowledgmentRepository.save(acknowledgment);
    } catch (Exception e) {
        // Log error but don't fail the status change
        System.err.println("Failed to create automatic acknowledgment: " + e.getMessage());
    }
}
```

### Manual Acknowledgment Process

```java
public LaptopDto manuallyAcknowledgeLaptop(UserDTO currentUser, Long laptopId, String acknowledgmentNotes) {
    // 1. Validate laptop exists and is in PENDING_ACKNOWLEDGMENT status
    // 2. Check user authorization for the station
    // 3. Update existing acknowledgment with manual details
    // 4. Change status from PENDING_ACKNOWLEDGMENT to ISSUED
    // 5. Return updated laptop
}
```

## Error Handling

### Automatic Acknowledgment Failures

If automatic acknowledgment creation fails:
- The laptop status change still succeeds
- Error is logged but not thrown
- Manual acknowledgment can still be performed later

### Manual Acknowledgment Validation

- **Laptop not found**: 404 Not Found
- **Wrong status**: 400 Bad Request (not in PENDING_ACKNOWLEDGMENT)
- **Unauthorized**: 403 Forbidden (user not authorized for station)
- **No acknowledgment found**: 404 Not Found

## Security Considerations

1. **Station Authorization**: Only users assigned to the laptop's station can acknowledge
2. **Status Validation**: Only laptops in `PENDING_ACKNOWLEDGMENT` status can be manually acknowledged
3. **Audit Trail**: All acknowledgments (automatic and manual) are logged with timestamps and user information

## Usage Examples

### Frontend Integration

```javascript
// Issue a laptop (creates automatic acknowledgment)
const issueLaptop = async (laptopId, assignmentData) => {
    const response = await fetch(`/laptop/${laptopId}/assignment`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(assignmentData)
    });
    // Status will be PENDING_ACKNOWLEDGMENT
};

// Manually acknowledge a laptop
const acknowledgeLaptop = async (laptopId, notes) => {
    const response = await fetch(`/laptop/${laptopId}/manual-acknowledge`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(notes)
    });
    // Status will be ISSUED
};

// Get pending acknowledgments for a station
const getPendingAcknowledgment = async (station) => {
    const response = await fetch(`/laptop/pending-acknowledgment/${station}`);
    return response.json();
};
```

## Benefits

1. **Automatic Workflow**: No manual intervention required for initial acknowledgment creation
2. **Audit Trail**: Complete history of all acknowledgments
3. **Station Control**: Station admins maintain control over final acknowledgment
4. **Error Resilience**: System continues to work even if acknowledgment creation fails
5. **Flexibility**: Supports both automatic and manual acknowledgment processes 