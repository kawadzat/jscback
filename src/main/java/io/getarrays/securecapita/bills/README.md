# Water Bill Management System

This module provides comprehensive water bill management functionality with WhatsApp and address delivery capabilities.

## Features

### Core Functionality
- **Water Bill Management**: Create, read, update, and delete water bills
- **Automatic Calculations**: Consumption, subtotal, and total amount calculations
- **Bill Number Generation**: Automatic unique bill number generation
- **Status Tracking**: Track bill status (PENDING, SENT, DELIVERED, PAID, OVERDUE, CANCELLED)

### Delivery Methods
- **WhatsApp Delivery**: Send bills directly to customer WhatsApp numbers
- **Postal Mail**: Send bills to customer home addresses
- **Email Delivery**: Send bills via email
- **SMS Delivery**: Send bills via SMS
- **Hand Delivery**: Mark bills for hand delivery

### Search and Filtering
- Search by customer name, phone, WhatsApp number, meter number
- Filter by status, delivery method, billing period
- Find overdue bills, high consumption bills, high amount bills
- Search by home address

### Reporting
- Count bills by status and delivery method
- Sum total amounts by status and delivery method
- Calculate outstanding and paid amounts
- Get average bill amounts and total consumption

### Validation
- Phone number validation
- WhatsApp number validation
- Bill number uniqueness validation
- Meter number validation
- Business logic validation (readings, dates, etc.)

## Database Schema

### water_bills Table
```sql
CREATE TABLE water_bills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_number VARCHAR(25) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    whatsapp_number VARCHAR(20) NOT NULL,
    home_address VARCHAR(500) NOT NULL,
    meter_number VARCHAR(50) NOT NULL,
    billing_period_start DATE NOT NULL,
    billing_period_end DATE NOT NULL,
    previous_reading DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    current_reading DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    consumption DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    rate_per_unit DECIMAL(7,2) NOT NULL DEFAULT 0.00,
    subtotal DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    due_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    delivery_method VARCHAR(20) NOT NULL DEFAULT 'WHATSAPP',
    sent_at TIMESTAMP NULL,
    delivery_status VARCHAR(50) NULL,
    delivery_message TEXT NULL,
    notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NULL,
    updated_by VARCHAR(100) NULL
);
```

## API Endpoints

### Basic CRUD Operations
- `POST /api/v1/water-bills` - Create a new water bill
- `GET /api/v1/water-bills/{id}` - Get water bill by ID
- `GET /api/v1/water-bills/bill-number/{billNumber}` - Get water bill by bill number
- `GET /api/v1/water-bills` - Get all water bills
- `PUT /api/v1/water-bills/{id}` - Update water bill
- `DELETE /api/v1/water-bills/{id}` - Delete water bill

### Bill Delivery
- `POST /api/v1/water-bills/{id}/send/whatsapp` - Send bill to WhatsApp
- `POST /api/v1/water-bills/{id}/send/address` - Send bill to address
- `POST /api/v1/water-bills/{id}/send` - Send bill with specified delivery method
- `POST /api/v1/water-bills/bulk/send` - Send multiple bills
- `POST /api/v1/water-bills/bulk/send/status` - Send bills by status

### Search and Filter
- `GET /api/v1/water-bills/customer-phone/{phone}` - Get bills by customer phone
- `GET /api/v1/water-bills/whatsapp-number/{whatsappNumber}` - Get bills by WhatsApp number
- `GET /api/v1/water-bills/meter-number/{meterNumber}` - Get bills by meter number
- `GET /api/v1/water-bills/status/{status}` - Get bills by status
- `GET /api/v1/water-bills/overdue` - Get overdue bills
- `GET /api/v1/water-bills/billing-period` - Get bills by billing period
- `GET /api/v1/water-bills/high-consumption` - Get high consumption bills
- `GET /api/v1/water-bills/high-amount` - Get high amount bills

### Business Logic
- `POST /api/v1/water-bills/generate` - Generate a new water bill
- `POST /api/v1/water-bills/{id}/calculate` - Calculate bill amounts
- `POST /api/v1/water-bills/{id}/mark-paid` - Mark bill as paid
- `POST /api/v1/water-bills/{id}/mark-overdue` - Mark bill as overdue

### Reporting
- `GET /api/v1/water-bills/stats/count-by-status/{status}` - Count bills by status
- `GET /api/v1/water-bills/stats/outstanding-amount` - Get total outstanding amount
- `GET /api/v1/water-bills/stats/paid-amount` - Get total paid amount
- `GET /api/v1/water-bills/stats/average-bill-amount` - Get average bill amount
- `GET /api/v1/water-bills/stats/total-consumption` - Get total consumption

### Validation
- `GET /api/v1/water-bills/validate/bill-number/{billNumber}` - Check if bill number exists
- `GET /api/v1/water-bills/validate/whatsapp-number/{whatsappNumber}` - Validate WhatsApp number
- `GET /api/v1/water-bills/validate/phone-number/{phoneNumber}` - Validate phone number
- `GET /api/v1/water-bills/validate/overdue/{id}` - Check if bill is overdue

## Usage Examples

### Create a Water Bill
```json
POST /api/v1/water-bills
{
    "customerName": "John Smith",
    "customerPhone": "+1234567890",
    "whatsappNumber": "+1234567890",
    "homeAddress": "123 Main Street, New York, NY 10001",
    "meterNumber": "MTR001234",
    "billingPeriodStart": "2024-11-01",
    "billingPeriodEnd": "2024-11-30",
    "previousReading": 1250.50,
    "currentReading": 1350.75,
    "ratePerUnit": 2.50,
    "dueDate": "2024-12-30",
    "deliveryMethod": "WHATSAPP"
}
```

### Send Bill to WhatsApp
```bash
POST /api/v1/water-bills/1/send/whatsapp
```

### Get Overdue Bills
```bash
GET /api/v1/water-bills/overdue
```

### Get Bill Statistics
```bash
GET /api/v1/water-bills/stats/outstanding-amount
```

## WhatsApp Integration

The system includes a WhatsApp service that can be integrated with WhatsApp Business API:

### Features
- Send water bills directly to customer WhatsApp numbers
- Bulk sending with rate limiting
- Delivery status tracking
- Retry logic with exponential backoff
- Template message support

### Integration Steps
1. Set up WhatsApp Business API account
2. Configure access token and phone number ID
3. Update `WhatsAppServiceImpl` with actual API calls
4. Test with sandbox environment first

### Example WhatsApp Message Format
```
💧 WATER BILL NOTIFICATION

Bill Number: WB2024120112000012345678
Customer: John Smith
Meter Number: MTR001234
Address: 123 Main Street, New York, NY 10001

Billing Period: 2024-11-01 to 2024-11-30
Previous Reading: 1250.50
Current Reading: 1350.75
Consumption: 100.25 units

Rate per Unit: $2.50
Subtotal: $250.63
Tax: $12.53
Total Amount: $263.16

Due Date: 2024-12-30

Please pay your water bill on time to avoid late fees.
For payment options, contact our customer service.

Thank you for choosing our water service!
```

## Configuration

### Application Properties
```yaml
# WhatsApp Business API Configuration
whatsapp:
  enabled: true
  access-token: your-access-token
  phone-number-id: your-phone-number-id
  api-version: v17.0
  base-url: https://graph.facebook.com

# Water Bill Configuration
water-bill:
  default-rate-per-unit: 2.50
  default-tax-rate: 0.05
  default-due-days: 30
  bill-number-prefix: WB
```

## Security Considerations

- Validate all input data
- Sanitize phone numbers and addresses
- Implement rate limiting for WhatsApp API
- Log all delivery attempts
- Handle API failures gracefully
- Secure API tokens and credentials

## Future Enhancements

- PDF bill generation
- Payment integration
- Customer portal
- Automated billing cycles
- Advanced analytics and reporting
- Multi-language support
- Mobile app integration 