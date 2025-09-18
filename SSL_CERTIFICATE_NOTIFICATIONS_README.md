# SSL Certificate Expiry Notification System

## Overview

The SSL Certificate Expiry Notification System automatically monitors SSL certificates in the JSC Backend system and sends email notifications to cyber security specialists and system administrators two months before certificates expire. This proactive approach helps prevent service disruptions caused by expired SSL certificates.

## Features

### 1. Automated Monitoring
- **Daily Checks**: Automatically runs every day at 9:00 AM
- **2-Month Advance Warning**: Notifies stakeholders 2 months before expiry
- **Smart Date Range**: Uses ±7 days window to account for daily scheduled tasks

### 2. Targeted Notifications
- **Cyber Security Specialists**: Receive notifications for security oversight
- **System Administrators**: Get alerts for infrastructure management
- **Role-Based Distribution**: Automatically finds users by role names

### 3. Professional Email Templates
- **HTML Formatting**: Professional, responsive email design
- **Certificate Details Table**: Comprehensive information display
- **Action Recommendations**: Clear guidance on next steps
- **Visual Indicators**: Color-coded urgency levels

## System Components

### 1. SslCertificateNotificationService
The core service that handles all notification logic and scheduling.

**Key Methods:**
- `checkSslCertificateExpiryAndNotify()` - Scheduled daily check
- `findCertificatesExpiringInTwoMonths()` - Certificate discovery
- `sendExpiryNotifications()` - Notification distribution
- `buildExpiryNotificationEmail()` - Email content generation

**Scheduling:**
```java
@Scheduled(cron = "0 0 9 * * ?") // Every day at 9:00 AM
public void checkSslCertificateExpiryAndNotify()
```

### 2. SslCertificateNotificationController
REST API endpoints for manual control and monitoring.

**Endpoints:**
- `POST /api/v1/ssl-certificate/notifications/check` - Manual trigger
- `GET /api/v1/ssl-certificate/notifications/expiring-count` - Get count
- `GET /api/v1/ssl-certificate/notifications/status` - System status

## Email Notification Details

### 1. Recipients
The system automatically identifies and notifies users with these roles:
- `CYBER_SECURITY_SPECIALIST`
- `SYSADMIN`

### 2. Email Content
Each notification includes:
- **Alert Header**: Clear indication of urgency
- **Certificate Table**: Detailed information for each expiring certificate
- **Days Until Expiry**: Calculated countdown
- **Recommended Actions**: Step-by-step guidance
- **Visual Styling**: Professional appearance with urgency indicators

## Configuration

### 1. Scheduling Configuration
The system uses Spring's `@Scheduled` annotation with cron expressions:

```java
// Daily at 9:00 AM
@Scheduled(cron = "0 0 9 * * ?")
```

### 2. Email Configuration
Uses existing email service configuration from `application.yml`.

## Usage Examples

### 1. Manual Notification Check
```bash
# Trigger manual check
curl -X POST http://localhost:8080/api/v1/ssl-certificate/notifications/check
```

### 2. Check Expiring Certificates Count
```bash
# Get count of certificates expiring in 2 months
curl -X GET http://localhost:8080/api/v1/ssl-certificate/notifications/expiring-count
```

### 3. System Status Check
```bash
# Get system status and configuration
curl -X GET http://localhost:8080/api/v1/ssl-certificate/notifications/status
```

## Conclusion

The SSL Certificate Expiry Notification System provides a robust, automated solution for preventing SSL certificate-related service disruptions. By proactively notifying cyber security specialists and system administrators two months before expiry, the system ensures timely certificate renewals and maintains system security and availability.


















