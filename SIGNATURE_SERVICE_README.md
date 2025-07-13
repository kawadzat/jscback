# Signature Service Documentation

## Overview

The Signature Service is a comprehensive digital signature management system integrated with the laptop acknowledgment workflow. It provides secure signature generation, validation, verification, and storage capabilities for laptop issuance acknowledgments.

## Features

### 1. Digital Signature Generation
- **Purpose**: Creates cryptographically secure digital signatures for laptop acknowledgments
- **Algorithm**: SHA-256 hashing with Base64 encoding
- **Components**: Hash + Timestamp + User ID + Laptop ID
- **Security**: Includes timestamp validation and user verification

### 2. Signature Validation
- **Real-time Validation**: Validates signatures against acknowledgment data
- **Timestamp Verification**: Ensures signatures are not older than 24 hours
- **User Verification**: Confirms signature belongs to the correct user
- **Data Integrity**: Verifies hash matches the acknowledgment payload

### 3. Signature Verification
- **Comprehensive Verification**: Full authenticity and integrity checks
- **Database Integration**: Retrieves acknowledgment data for verification
- **Detailed Results**: Returns verification status with metadata
- **Error Handling**: Provides specific error messages for failed verifications

### 4. Signature Statistics
- **User Analytics**: Tracks signature statistics per user
- **Validity Rates**: Calculates percentage of valid signatures
- **Historical Data**: Maintains signature history and timestamps
- **Performance Metrics**: Total, valid, and invalid signature counts

## Architecture

### Core Components

1. **SignatureService**: Main service class handling all signature operations
2. **SignatureController**: REST API endpoints for signature operations
3. **SignatureVerificationResult**: DTO for verification results
4. **SignatureStatistics**: DTO for user signature statistics
5. **LaptopAcknowledgmentRepository**: Database operations for acknowledgments

### Integration Points

- **LaptopService**: Integrated for automatic signature hash generation
- **User Management**: Uses existing user authentication and authorization
- **Database**: Leverages existing laptop acknowledgment entities

## API Endpoints

### 1. Generate Signature
```http
POST /api/v1/signatures/generate
Content-Type: application/json

{
  "laptopId": 123,
  "notes": "Acknowledgment notes",
  "signatureType": "DIGITAL_SIGNATURE",
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0..."
}
```

### 2. Validate Signature
```http
POST /api/v1/signatures/validate
Content-Type: application/json

{
  "signature": "base64EncodedSignature",
  "acknowledgmentDto": {
    "laptopId": 123,
    "notes": "Acknowledgment notes"
  }
}
```

### 3. Verify Signature
```http
POST /api/v1/signatures/verify/{laptopId}
Content-Type: application/json

{
  "signature": "base64EncodedSignature"
}
```

### 4. Generate Signature Hash
```http
POST /api/v1/signatures/hash
Content-Type: application/json

{
  "signature": "base64EncodedSignature",
  "laptopId": 123,
  "userId": 456
}
```

### 5. Get Signature Statistics
```http
GET /api/v1/signatures/statistics
Authorization: Bearer <token>
```

### 6. Get Signature Metadata
```http
GET /api/v1/signatures/metadata/{laptopId}
Authorization: Bearer <token>
```

## Security Features

### 1. Cryptographic Security
- **SHA-256 Hashing**: Industry-standard cryptographic hash function
- **Base64 Encoding**: Secure encoding for signature transmission
- **Timestamp Validation**: Prevents replay attacks
- **User Verification**: Ensures signature authenticity

### 2. Data Integrity
- **Payload Verification**: Validates all acknowledgment data
- **Hash Matching**: Ensures data hasn't been tampered with
- **Database Consistency**: Verifies against stored acknowledgment records

### 3. Access Control
- **Authentication Required**: All endpoints require valid user authentication
- **Authorization Checks**: Verifies user permissions for operations
- **Station-based Access**: Restricts operations to authorized stations

## Usage Examples

### 1. Basic Signature Generation
```java
// In LaptopService.acknowledgeLaptopIssuance()
String signature = signatureService.generateSignature(acknowledgmentDto, currentUser);
String signatureHash = signatureService.generateSignatureHash(signature, laptopId, currentUser.getId());
```

### 2. Signature Validation
```java
boolean isValid = signatureService.validateSignature(signature, acknowledgmentDto, currentUser);
if (isValid) {
    // Proceed with acknowledgment
} else {
    // Handle invalid signature
}
```

### 3. Signature Verification
```java
SignatureVerificationResult result = signatureService.verifySignature(laptopId, signature);
if (result.isValid()) {
    UserDTO signedBy = result.getSignedBy();
    LocalDateTime timestamp = result.getSignatureTimestamp();
    // Process verified signature
} else {
    String error = result.getErrorMessage();
    // Handle verification failure
}
```

### 4. Statistics Retrieval
```java
SignatureStatistics stats = signatureService.getSignatureStatistics(currentUser);
long totalSignatures = stats.getTotalSignatures();
double validityRate = stats.getValidityRate();
LocalDateTime lastSignature = stats.getLastSignatureDate();
```

## Database Schema

### LaptopAcknowledgment Entity
```sql
-- Signature-related fields in laptopAcknowledgment table
signature VARCHAR(MAX),           -- Base64-encoded signature
signature_type VARCHAR(50),       -- Signature type (DIGITAL_SIGNATURE, etc.)
signature_timestamp TIMESTAMP,    -- When signature was created
signature_hash VARCHAR(255),      -- Hash for verification
ip_address VARCHAR(45),           -- IP address of signer
user_agent VARCHAR(500),          -- Browser/device info
certificate_info VARCHAR(1000)    -- Digital certificate info
```

## Error Handling

### Common Error Scenarios
1. **Invalid Signature Format**: Malformed or corrupted signatures
2. **Expired Signatures**: Signatures older than 24 hours
3. **User Mismatch**: Signature doesn't match the current user
4. **Data Tampering**: Hash doesn't match the acknowledgment data
5. **Database Errors**: Missing acknowledgment records

### Error Response Format
```json
{
  "message": "Error description",
  "data": null,
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## Best Practices

### 1. Signature Generation
- Always validate input data before generating signatures
- Include all relevant acknowledgment data in the signature payload
- Use secure random number generation for additional entropy
- Store signature hashes for future verification

### 2. Signature Validation
- Validate signatures immediately after generation
- Check timestamp validity to prevent replay attacks
- Verify user permissions before accepting signatures
- Log all validation attempts for audit purposes

### 3. Security Considerations
- Use HTTPS for all signature-related API calls
- Implement rate limiting to prevent abuse
- Regularly rotate cryptographic keys if applicable
- Monitor for suspicious signature patterns

## Integration with Existing System

### Laptop Acknowledgment Workflow
1. **Issue Laptop**: Laptop is issued with PENDING_ACKNOWLEDGMENT status
2. **Station Admin Review**: Station admin reviews laptop details
3. **Signature Generation**: Digital signature is generated for acknowledgment
4. **Hash Storage**: Signature hash is stored in database
5. **Status Update**: Laptop status changes to ISSUED
6. **Verification**: Signature can be verified later for audit purposes

### Automatic Integration
- Signature hash generation is automatically triggered during acknowledgment
- No manual intervention required for basic signature operations
- Seamless integration with existing laptop management workflow

## Future Enhancements

### Planned Features
1. **Digital Certificates**: Integration with PKI for enhanced security
2. **Batch Verification**: Bulk signature verification capabilities
3. **Audit Logging**: Comprehensive audit trail for all signature operations
4. **Signature Templates**: Predefined signature templates for different scenarios
5. **Mobile Support**: Enhanced mobile signature capabilities

### Performance Optimizations
1. **Caching**: Signature verification result caching
2. **Async Processing**: Background signature validation
3. **Database Indexing**: Optimized queries for signature operations
4. **Compression**: Efficient signature storage and transmission

## Troubleshooting

### Common Issues
1. **Signature Validation Fails**: Check timestamp and user permissions
2. **Hash Mismatch**: Verify acknowledgment data hasn't changed
3. **Database Errors**: Ensure acknowledgment records exist
4. **Performance Issues**: Monitor database query performance

### Debug Information
- Enable debug logging for detailed signature operation logs
- Check database connection and transaction management
- Verify user authentication and authorization status
- Monitor system resources during signature operations 