# Kafka Integration for Laptop Inventory System

## Overview

This document describes the Kafka integration added to the laptop inventory system to enable event-driven architecture and real-time event processing.

## Features

- **Event Publishing**: Automatically publishes events for key laptop operations
- **Event Consumption**: Processes events for analytics, notifications, and integrations
- **Real-time Processing**: Enables real-time updates and notifications
- **Scalability**: Supports high-throughput event processing

## Kafka Topics

### 1. `laptop-issued`
**Purpose**: Published when a laptop is issued to an employee
**Event Type**: `LAPTOP_ISSUED`
**Key Data**:
- Laptop details (ID, serial number, manufacturer)
- Assignment details (issued to, station, department)
- Issuer information
- Timestamp

### 2. `laptop-acknowledged`
**Purpose**: Published when a laptop acknowledgment is completed
**Event Type**: `LAPTOP_ACKNOWLEDGED`
**Key Data**:
- Laptop details
- Acknowledgment details (notes, signature type)
- Acknowledger information
- IP address and user agent

### 3. `laptop-status-changed`
**Purpose**: Published when laptop status changes
**Event Type**: `LAPTOP_STATUS_CHANGED`
**Key Data**:
- Previous and new status
- Laptop details
- Change timestamp

### 4. `laptop-maintenance`
**Purpose**: Published for maintenance-related events
**Event Type**: `LAPTOP_MAINTENANCE`
**Key Data**:
- Maintenance type and description
- Laptop details
- Maintenance timestamp

## Configuration

### Application Properties
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: laptop-inventory-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: all
      retries: 3
      batch-size: 16384
      linger-ms: 1
      buffer-memory: 33554432
```

### Dependencies
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka-test</artifactId>
    <scope>test</scope>
</dependency>
```

## Implementation Details

### Event DTO Structure
```java
@Data
@Builder
public class LaptopEventDto {
    private String eventType;
    private Long laptopId;
    private String serialNumber;
    private String manufacturer;
    private String issuedTo;
    private String station;
    private String department;
    private String email;
    private String status;
    private String notes;
    private String acknowledgedBy;
    private LocalDateTime eventTimestamp;
    private String issueDate;
    private String replacementDate;
    private String issuerEmail;
    private String signatureType;
    private String ipAddress;
    private String userAgent;
}
```

### Key Components

1. **KafkaConfig**: Configuration class for Kafka producers, consumers, and topics
2. **KafkaEventService**: Service for publishing events to Kafka topics
3. **KafkaConsumerService**: Service for consuming and processing events
4. **LaptopEventDto**: Data transfer object for event payloads

## Usage Examples

### Publishing Events
Events are automatically published when:
- A laptop is issued (`issueLaptop` method)
- A laptop is acknowledged (`acknowledgeLaptopIssuance` method)
- Laptop status changes (`changeLaptopStatus` method)

### Consuming Events
```java
@KafkaListener(topics = "laptop-issued", groupId = "laptop-inventory-group")
public void handleLaptopIssuedEvent(LaptopEventDto event) {
    log.info("Received laptop issued event: {}", event);
    // Add your business logic here
}
```

## Setup Instructions

### 1. Install Kafka
```bash
# Download Kafka
wget https://downloads.apache.org/kafka/3.5.1/kafka_2.13-3.5.1.tgz
tar -xzf kafka_2.13-3.5.1.tgz
cd kafka_2.13-3.5.1
```

### 2. Start Kafka
```bash
# Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka (in new terminal)
bin/kafka-server-start.sh config/server.properties
```

### 3. Create Topics (Optional - Auto-created by Spring)
```bash
bin/kafka-topics.sh --create --topic laptop-issued --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
bin/kafka-topics.sh --create --topic laptop-acknowledged --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
bin/kafka-topics.sh --create --topic laptop-status-changed --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
bin/kafka-topics.sh --create --topic laptop-maintenance --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
```

### 4. Monitor Topics
```bash
# List topics
bin/kafka-topics.sh --list --bootstrap-server localhost:9092

# Monitor messages
bin/kafka-console-consumer.sh --topic laptop-issued --from-beginning --bootstrap-server localhost:9092
```

## Event Flow

1. **Laptop Operation**: User performs laptop operation (issue, acknowledge, status change)
2. **Event Creation**: System creates `LaptopEventDto` with relevant data
3. **Event Publishing**: Event is published to appropriate Kafka topic
4. **Event Processing**: Consumers process events for various purposes
5. **Business Logic**: Execute business logic based on event type

## Benefits

1. **Decoupling**: Services are decoupled through event-driven communication
2. **Scalability**: Easy to scale by adding more consumers
3. **Real-time**: Enables real-time processing and notifications
4. **Audit Trail**: Complete audit trail of all laptop operations
5. **Integration**: Easy integration with external systems
6. **Analytics**: Enables real-time analytics and reporting

## Monitoring and Debugging

### Logs
Events are logged with details:
```
INFO: Event sent successfully to topic: laptop-issued, partition: 0, offset: 123
INFO: Received laptop issued event: LaptopEventDto{eventType='LAPTOP_ISSUED', ...}
```

### Error Handling
- Failed event publishing is logged with error details
- Consumer errors are logged and can be configured for retry
- Dead letter queues can be configured for failed messages

## Future Enhancements

1. **Event Sourcing**: Use events as source of truth
2. **CQRS**: Separate read and write models
3. **Saga Pattern**: Implement distributed transactions
4. **Event Store**: Persistent event storage
5. **Stream Processing**: Real-time analytics with Kafka Streams 