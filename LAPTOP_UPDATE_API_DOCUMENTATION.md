# Laptop Update API Documentation

## Overview

This document describes all the PUT endpoints available for updating laptop information in the system. Each endpoint is designed for specific update scenarios with proper validation and error handling.

## Base URL
```
PUT /api/v1/laptop/{id}
```

## Available Update Endpoints

### 1. Full Laptop Update
**Endpoint:** `PUT /laptop/{id}`

**Description:** Updates all laptop fields with comprehensive validation.

**Request Body:**
```json
{
  "purchaseDate": "2024-01-15",
  "manufacturer": "Dell",
  "serialNumber": "LAP-2024-001",
  "ram": 16,
  "processor": 8,
  "issueDate": "2024-01-20",
  "status": "ISSUED",
  "issuedTo": "John Doe",
  "station": "IT Department",
  "department": "Information Technology",
  "designation": "Software Developer",
  "email": "john.doe@company.com",
  "replacementDate": "2027-01-15"
}
```

**Response:**
```json
{
  "message": "Laptop Updated Successfully",
  "data": {
    "id": 1,
    "purchaseDate": "2024-01-15",
    "manufacturer": "Dell",
    "serialNumber": "LAP-2024-001",
    "ram": 16,
    "processor": 8,
    "issueDate": "2024-01-20",
    "status": "ISSUED",
    "issuedTo": "John Doe",
    "station": "IT Department",
    "department": "Information Technology",
    "designation": "Software Developer",
    "email": "john.doe@company.com",
    "replacementDate": "2027-01-15"
  }
}
```

**Validation Rules:**
- Serial number must be unique (if changed)
- All required fields must be provided
- Date formats must be valid (yyyy-MM-dd)
- RAM must be between 1-1024 GB
- Processor must be positive integer
- Email must be valid format

### 2. Status Update Only
**Endpoint:** `PUT /laptop/{id}/status`

**Description:** Updates only the laptop status.

**Request Body:**
```json
"ISSUED"
```

**Available Status Values:**
- `WAITING_FOR_ACKNOWLEDGEMENT` - Laptop is waiting for acknowledgment from station admin
- `AVAILABLE` - Laptop is available for issue
- `ISSUED` - Laptop is currently issued to someone
- `PENDING_ACKNOWLEDGMENT` - Waiting for station admin acknowledgment
- `MAINTENANCE` - Laptop is under maintenance
- `RETIRED` - Laptop is no longer in use

**Response:**
```json
{
  "message": "Laptop Status Updated Successfully",
  "data": {
    "id": 1,
    "status": "ISSUED",
    // ... other laptop fields
  }
}
```

### 3. Assignment Update Only
**Endpoint:** `PUT /laptop/{id}/assignment`

**Description:** Updates only the assignment-related fields (issuedTo, station, department, designation, email).

**Request Body:**
```json
{
  "issuedTo": "Jane Smith",
  "station": "HR Department",
  "department": "Human Resources",
  "designation": "HR Manager",
  "email": "jane.smith@company.com"
}
```

**Response:**
```json
{
  "message": "Laptop Assignment Updated Successfully",
  "data": {
    "id": 1,
    "issuedTo": "Jane Smith",
    "station": "HR Department",
    "department": "Human Resources",
    "designation": "HR Manager",
    "email": "jane.smith@company.com",
    // ... other laptop fields
  }
}
```

**Validation Rules:**
- All fields are required
- Email must be valid format
- Station, department, and designation must not exceed 100 characters

### 4. Specifications Update Only
**Endpoint:** `PUT /laptop/{id}/specifications`

**Description:** Updates only the hardware specifications (RAM, processor).

**Request Body:**
```json
{
  "ram": 32,
  "processor": 12
}
```

**Response:**
```json
{
  "message": "Laptop Specifications Updated Successfully",
  "data": {
    "id": 1,
    "ram": 32,
    "processor": 12,
    // ... other laptop fields
  }
}
```

**Validation Rules:**
- RAM must be between 1-1024 GB
- Processor must be positive integer

### 5. Issue with Acknowledgment
**Endpoint:** `PUT /laptop/{id}/issue-with-acknowledgment`

**Description:** Issues a laptop and sets status to PENDING_ACKNOWLEDGMENT for station admin approval.

**Request Body:**
```json
{
  "issuedTo": "John Doe",
  "station": "IT Department",
  "department": "Information Technology",
  "designation": "Software Developer",
  "email": "john.doe@company.com",
  "issueDate": "2024-01-20"
}
```

**Response:**
```json
{
  "message": "Laptop issued and pending station admin acknowledgment",
  "data": {
    "id": 1,
    "status": "PENDING_ACKNOWLEDGMENT",
    // ... other laptop fields
  }
}
```

### 6. Action-Based Status Update
**Endpoint:** `PUT /laptop/{id}/action`

**Description:** Simple action-based endpoint that accepts string commands to update laptop status or refresh data.

**Request Body:**
```json
"issued"
```

**Available Actions:**
- `update` or `refresh` - Refresh laptop data
- `available` - Set status to AVAILABLE
- `issued` - Set status to ISSUED
- `maintenance` - Set status to MAINTENANCE
- `retired` - Set status to RETIRED
- `waiting` or `waiting_for_acknowledgement` - Set status to WAITING_FOR_ACKNOWLEDGEMENT

**Response:**
```json
{
  "message": "Laptop status changed to ISSUED",
  "data": {
    "id": 1,
    "status": "ISSUED",
    // ... other laptop fields
  }
}
```

**Error Response:**
```json
{
  "message": "Invalid action: invalid_action. Valid actions are: update, refresh, available, issued, maintenance, retired, waiting"
}
```

## Error Responses

### 404 Not Found
```json
{
  "message": "Laptop not found with id: 999"
}
```

### 409 Conflict (Serial Number)
```json
{
  "message": "Serial number already exists: LAP-2024-001"
}
```

### 400 Bad Request
```json
{
  "message": "Invalid data: RAM must be at least 1 GB"
}
```

### 500 Internal Server Error
```json
{
  "message": "Error updating laptop: Database connection failed"
}
```

## Usage Examples

### Frontend JavaScript Example
```javascript
// Update full laptop
const updateLaptop = async (laptopId, laptopData) => {
  try {
    const response = await fetch(`/api/v1/laptop/${laptopId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(laptopData)
    });
    
    if (response.ok) {
      const result = await response.json();
      console.log('Laptop updated:', result.data);
      return result.data;
    } else {
      const error = await response.json();
      throw new Error(error.message);
    }
  } catch (error) {
    console.error('Error updating laptop:', error);
    throw error;
  }
};

// Update assignment only
const updateAssignment = async (laptopId, assignmentData) => {
  const response = await fetch(`/api/v1/laptop/${laptopId}/assignment`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(assignmentData)
  });
  
  return response.json();
};

// Update status only
const updateStatus = async (laptopId, status) => {
  const response = await fetch(`/api/v1/laptop/${laptopId}/status`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(status)
  });
  
  return response.json();
};

// Action-based status update
const performAction = async (laptopId, action) => {
  const response = await fetch(`/api/v1/laptop/${laptopId}/action`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(action)
  });
  
  return response.json();
};
```

### cURL Examples
```bash
# Update full laptop
curl -X PUT "http://localhost:8080/api/v1/laptop/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "purchaseDate": "2024-01-15",
    "manufacturer": "Dell",
    "serialNumber": "LAP-2024-001",
    "ram": 16,
    "processor": 8,
    "issueDate": "2024-01-20",
    "status": "ISSUED",
    "issuedTo": "John Doe",
    "station": "IT Department",
    "department": "Information Technology",
    "designation": "Software Developer",
    "email": "john.doe@company.com",
    "replacementDate": "2027-01-15"
  }'

# Update assignment only
curl -X PUT "http://localhost:8080/api/v1/laptop/1/assignment" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "issuedTo": "Jane Smith",
    "station": "HR Department",
    "department": "Human Resources",
    "designation": "HR Manager",
    "email": "jane.smith@company.com"
  }'

# Update status only
curl -X PUT "http://localhost:8080/api/v1/laptop/1/status" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '"MAINTENANCE"'

# Action-based status update
curl -X PUT "http://localhost:8080/api/v1/laptop/1/action" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '"waiting"'
```

## Security Considerations

1. **Authentication Required**: All endpoints require valid JWT token
2. **Authorization**: Users can only update laptops they have permission for
3. **Input Validation**: All inputs are validated server-side
4. **Serial Number Uniqueness**: System prevents duplicate serial numbers
5. **Audit Trail**: All updates are logged for audit purposes

## Best Practices

1. **Use Specific Endpoints**: Use targeted endpoints (assignment, specifications) instead of full update when possible
2. **Validate Client-Side**: Implement client-side validation for better UX
3. **Handle Errors Gracefully**: Always handle error responses in your frontend
4. **Check Response Status**: Verify the response status before processing data
5. **Log Updates**: Consider logging all update operations for audit purposes 