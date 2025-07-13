# Antivirus Countdown System

This document describes the comprehensive date and time countdown system implemented for antivirus license expiration tracking.

## Overview

The antivirus countdown system provides detailed time-based tracking for antivirus license expirations, including:
- Real-time countdown calculations (days, hours, minutes, seconds)
- Urgency level classification
- Bulk countdown statistics
- Dashboard data aggregation
- REST API endpoints for countdown information

## Components

### 1. AntivirusCountdownService
Core service that handles all countdown calculations and statistics.

**Key Methods:**
- `getDetailedCountdown(Antivirus antivirus)` - Get detailed countdown for single antivirus
- `getBulkCountdown(List<Antivirus> antivirusList)` - Get countdown for multiple antivirus entries
- `getCountdownStats(List<Antivirus> antivirusList)` - Get statistical overview

### 2. AntivirusCountdownDto
Data Transfer Object containing countdown information.

**Fields:**
- Basic antivirus info (ID, name, key, vendor, version, status)
- Countdown data (days, hours, minutes, seconds)
- Urgency level and formatted countdown string
- Laptop information (ID, serial number, user)

### 3. AntivirusCountdownMapper
Utility class for converting between entities and DTOs.

### 4. AntivirusController (Updated)
Enhanced controller with countdown-specific endpoints.

## API Endpoints

### Countdown Endpoints

#### 1. Get Single Countdown
```
GET /antivirus/{id}/countdown
```
Returns detailed countdown information for a specific antivirus.

**Response:**
```json
{
  "hasExpirationDate": true,
  "isExpired": false,
  "days": 5,
  "hours": 12,
  "minutes": 30,
  "seconds": 45,
  "urgencyLevel": "MEDIUM",
  "formattedCountdown": "5 days, 12 hours, 30 minutes, 45 seconds",
  "totalHours": 132,
  "totalMinutes": 7950,
  "totalSeconds": 477045,
  "expirationDate": "2024-01-15T10:30:00",
  "currentDate": "2024-01-10T22:00:15"
}
```

#### 2. Get All Countdowns
```
GET /antivirus/countdown/all
```
Returns countdown information for all antivirus entries with summary statistics.

#### 3. Get Countdown Statistics
```
GET /antivirus/countdown/stats
```
Returns statistical overview of all antivirus countdowns.

**Response:**
```json
{
  "totalExpiringIn24Hours": 3,
  "totalExpiringIn7Days": 15,
  "totalExpiringIn30Days": 45,
  "totalExpired": 8,
  "totalNoExpirationDate": 5,
  "totalWithExpirationDate": 95
}
```

#### 4. Get Expiring Soon
```
GET /antivirus/countdown/expiring-soon?days=7
```
Returns antivirus entries expiring within specified days (default: 7 days).

#### 5. Get Expired
```
GET /antivirus/countdown/expired
```
Returns all expired antivirus entries.

#### 6. Get Urgent (24 hours)
```
GET /antivirus/countdown/urgent
```
Returns antivirus entries expiring within 24 hours.

#### 7. Get By Urgency Level
```
GET /antivirus/countdown/by-urgency/{level}
```
Returns antivirus entries by urgency level (CRITICAL, HIGH, MEDIUM, LOW, EXPIRED).

#### 8. Get Dashboard Data
```
GET /antivirus/countdown/dashboard
```
Returns comprehensive dashboard data including stats, urgent items, expiring soon, and expired items.

### Laptop-Antivirus Endpoints

#### 9. Get Laptop with Antivirus
```
GET /antivirus/laptop/{laptopId}/with-antivirus
```
Returns a specific laptop with all its associated antivirus software.

**Response:**
```json
{
  "laptop": {
    "id": 1,
    "manufacturer": "Dell",
    "serialNumber": "DELL123456",
    "ram": 16,
    "processor": 8,
    "status": "ISSUED",
    "issuedTo": "John Doe",
    "department": "IT",
    "email": "john.doe@company.com"
  },
  "antivirusList": [
    {
      "id": 1,
      "name": "Norton Antivirus",
      "key": "NORTON-KEY-123",
      "vendor": "Norton",
      "version": "2024.1",
      "status": "ACTIVE",
      "isInstalled": true,
      "licenseExpirationDate": "2024-12-31T23:59:59",
      "daysToExpiration": 45
    }
  ],
  "antivirusCount": 1
}
```

#### 10. Get All Laptops with Antivirus
```
GET /antivirus/laptops/with-antivirus
```
Returns all laptops with their associated antivirus software.

## Urgency Levels

The system classifies antivirus licenses into urgency levels:

- **EXPIRED**: License has already expired
- **CRITICAL**: Expires within 24 hours
- **HIGH**: Expires within 7 days
- **MEDIUM**: Expires within 30 days
- **LOW**: Expires in more than 30 days

## Usage Examples

### 1. Get Countdown for Specific Antivirus
```java
@Autowired
private AntivirusCountdownService countdownService;

public void checkCountdown(Long antivirusId) {
    Antivirus antivirus = antivirusService.getById(antivirusId);
    Map<String, Object> countdown = countdownService.getDetailedCountdown(antivirus);
    
    System.out.println("Days remaining: " + countdown.get("days"));
    System.out.println("Urgency level: " + countdown.get("urgencyLevel"));
    System.out.println("Formatted: " + countdown.get("formattedCountdown"));
}
```

### 2. Get Bulk Statistics
```java
@Autowired
private AntivirusCountdownService countdownService;

public void getStatistics() {
    List<Antivirus> allAntivirus = antivirusService.getAll();
    Map<String, Object> stats = countdownService.getCountdownStats(allAntivirus);
    
    System.out.println("Expiring in 24 hours: " + stats.get("totalExpiringIn24Hours"));
    System.out.println("Total expired: " + stats.get("totalExpired"));
}
```

### 3. Convert to DTO
```java
@Autowired
private AntivirusCountdownMapper countdownMapper;

public void convertToDto() {
    List<Antivirus> antivirusList = antivirusService.getAll();
    List<AntivirusCountdownDto> dtos = countdownMapper.toCountdownDtoList(antivirusList);
    
    for (AntivirusCountdownDto dto : dtos) {
        System.out.println(dto.getAntivirusName() + ": " + dto.getFormattedCountdown());
    }
}
```

## Database Schema

The countdown system uses the existing `antivirus` table with the `license_expiration_date` field:

```sql
CREATE TABLE antivirus (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    `key` VARCHAR(255) NOT NULL UNIQUE,
    renew_time_interval INTEGER,
    version VARCHAR(255),
    vendor VARCHAR(255),
    status VARCHAR(50),
    is_installed BOOLEAN,
    license_expiration_date DATETIME,
    days_to_expiration BIGINT,
    laptop_id BIGINT,
    -- ... other fields
);
```

## Features

### 1. Real-time Calculations
- All countdown calculations are performed in real-time
- No cached values - always current
- Handles timezone differences properly

### 2. Comprehensive Time Breakdown
- Days, hours, minutes, and seconds remaining
- Total time in different units
- Formatted human-readable strings

### 3. Urgency Classification
- Automatic urgency level assignment
- Configurable thresholds
- Easy filtering by urgency

### 4. Bulk Operations
- Process multiple antivirus entries efficiently
- Statistical summaries
- Dashboard data aggregation

### 5. REST API Integration
- Full REST API support
- JSON responses
- Query parameter support
- Error handling

## Error Handling

The system handles various edge cases:

- **No expiration date**: Returns appropriate message
- **Already expired**: Shows days since expiration
- **Invalid dates**: Graceful error handling
- **Null values**: Safe null checking

## Performance Considerations

- Calculations are performed on-demand
- No database queries for countdown calculations
- Efficient streaming operations for bulk processing
- Minimal memory footprint

## Future Enhancements

Potential improvements for the countdown system:

1. **Caching**: Add caching for frequently accessed countdown data
2. **Notifications**: Integrate with notification system for expiring licenses
3. **Scheduling**: Add scheduled tasks for countdown updates
4. **Reports**: Generate countdown reports in various formats
5. **Alerts**: Email/SMS alerts for critical expirations
6. **Dashboard**: Web-based dashboard with real-time updates

## Testing

Use the `AntivirusCountdownExample` class to test the functionality:

```java
@Autowired
private AntivirusCountdownExample example;

public void testCountdown() {
    example.runAllExamples();
}
```

This will create sample data and demonstrate all countdown features. 