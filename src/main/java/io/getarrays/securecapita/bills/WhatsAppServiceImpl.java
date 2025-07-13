package io.getarrays.securecapita.bills;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class WhatsAppServiceImpl implements WhatsAppService {

    private static final Pattern WHATSAPP_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    
    // TODO: Add WhatsApp Business API configuration
    // private final WhatsAppBusinessApi whatsAppApi;
    // private final String accessToken;
    // private final String phoneNumberId;

    @Override
    public boolean sendMessage(String whatsappNumber, String message) {
        if (!isValidWhatsAppNumber(whatsappNumber)) {
            log.error("Invalid WhatsApp number: {}", whatsappNumber);
            return false;
        }

        if (!isServiceAvailable()) {
            log.warn("WhatsApp service is not available");
            return false;
        }

        try {
            // TODO: Implement actual WhatsApp Business API call
            // Example implementation:
            /*
            WhatsAppMessageRequest request = WhatsAppMessageRequest.builder()
                .to(whatsappNumber)
                .type("text")
                .text(new TextMessage(message))
                .build();
            
            WhatsAppResponse response = whatsAppApi.sendMessage(phoneNumberId, request);
            return response.isSuccess();
            */
            
            // Simulated implementation for now
            log.info("Sending WhatsApp message to {}: {}", whatsappNumber, message);
            
            // Simulate API delay
            Thread.sleep(1000);
            
            // Simulate success (90% success rate)
            boolean success = Math.random() > 0.1;
            
            if (success) {
                log.info("WhatsApp message sent successfully to {}", whatsappNumber);
            } else {
                log.warn("Failed to send WhatsApp message to {}", whatsappNumber);
            }
            
            return success;
            
        } catch (Exception e) {
            log.error("Error sending WhatsApp message to {}: {}", whatsappNumber, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendWaterBill(WaterBill waterBill) {
        if (waterBill == null) {
            log.error("Water bill is null");
            return false;
        }

        String formattedMessage = formatWaterBillForWhatsApp(waterBill);
        return sendMessage(waterBill.getWhatsappNumber(), formattedMessage);
    }

    @Override
    public int sendBulkWaterBills(List<WaterBill> waterBills) {
        if (waterBills == null || waterBills.isEmpty()) {
            log.warn("No water bills provided for bulk sending");
            return 0;
        }

        int successCount = 0;
        int totalCount = waterBills.size();

        log.info("Starting bulk WhatsApp sending for {} water bills", totalCount);

        for (WaterBill waterBill : waterBills) {
            try {
                boolean success = sendWaterBill(waterBill);
                if (success) {
                    successCount++;
                }
                
                // Add delay between messages to avoid rate limiting
                Thread.sleep(500);
                
            } catch (Exception e) {
                log.error("Error sending water bill {} to WhatsApp: {}", 
                    waterBill.getBillNumber(), e.getMessage());
            }
        }

        log.info("Bulk WhatsApp sending completed: {}/{} messages sent successfully", 
            successCount, totalCount);

        return successCount;
    }

    @Override
    public boolean isValidWhatsAppNumber(String whatsappNumber) {
        if (whatsappNumber == null || whatsappNumber.trim().isEmpty()) {
            return false;
        }
        
        // Remove any spaces or special characters
        String cleanNumber = whatsappNumber.replaceAll("[\\s\\-\\(\\)]", "");
        
        return WHATSAPP_PATTERN.matcher(cleanNumber).matches();
    }

    @Override
    public String getDeliveryStatus(String messageId) {
        if (messageId == null || messageId.trim().isEmpty()) {
            return "INVALID_MESSAGE_ID";
        }

        try {
            // TODO: Implement actual WhatsApp Business API status check
            // Example implementation:
            /*
            WhatsAppMessageStatus status = whatsAppApi.getMessageStatus(messageId);
            return status.getStatus();
            */
            
            // Simulated implementation for now
            log.info("Checking delivery status for message ID: {}", messageId);
            
            // Simulate different statuses
            String[] statuses = {"SENT", "DELIVERED", "READ", "FAILED"};
            String randomStatus = statuses[(int) (Math.random() * statuses.length)];
            
            log.info("Message ID {} status: {}", messageId, randomStatus);
            
            return randomStatus;
            
        } catch (Exception e) {
            log.error("Error checking delivery status for message ID {}: {}", 
                messageId, e.getMessage());
            return "ERROR";
        }
    }

    @Override
    public boolean isServiceAvailable() {
        // TODO: Implement actual service availability check
        // Example implementation:
        /*
        try {
            WhatsAppHealthCheck healthCheck = whatsAppApi.healthCheck();
            return healthCheck.isHealthy();
        } catch (Exception e) {
            return false;
        }
        */
        
        // Simulated implementation for now
        return true; // Assume service is available
    }

    /**
     * Format water bill for WhatsApp message
     */
    private String formatWaterBillForWhatsApp(WaterBill waterBill) {
        return String.format("""
            💧 *WATER BILL NOTIFICATION*
            
            *Bill Number:* %s
            *Customer:* %s
            *Meter Number:* %s
            *Address:* %s
            
            *Billing Period:* %s to %s
            *Previous Reading:* %s
            *Current Reading:* %s
            *Consumption:* %s units
            
            *Rate per Unit:* $%s
            *Subtotal:* $%s
            *Tax:* $%s
            *Total Amount:* $%s
            
            *Due Date:* %s
            
            Please pay your water bill on time to avoid late fees.
            For payment options, contact our customer service.
            
            Thank you for choosing our water service!
            """,
            waterBill.getBillNumber(),
            waterBill.getCustomerName(),
            waterBill.getMeterNumber(),
            waterBill.getHomeAddress(),
            waterBill.getBillingPeriodStart().toString(),
            waterBill.getBillingPeriodEnd().toString(),
            waterBill.getPreviousReading(),
            waterBill.getCurrentReading(),
            waterBill.getConsumption(),
            waterBill.getRatePerUnit(),
            waterBill.getSubtotal(),
            waterBill.getTax(),
            waterBill.getTotalAmount(),
            waterBill.getDueDate().toString()
        );
    }

    /**
     * Send WhatsApp message with retry logic
     */
    public boolean sendMessageWithRetry(String whatsappNumber, String message, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                boolean success = sendMessage(whatsappNumber, message);
                if (success) {
                    return true;
                }
                
                log.warn("Attempt {} failed for WhatsApp number: {}", attempt, whatsappNumber);
                
                if (attempt < maxRetries) {
                    // Exponential backoff
                    long delay = (long) Math.pow(2, attempt) * 1000; // 2^attempt seconds
                    Thread.sleep(delay);
                }
                
            } catch (Exception e) {
                log.error("Error in attempt {} for WhatsApp number {}: {}", 
                    attempt, whatsappNumber, e.getMessage());
            }
        }
        
        log.error("All {} attempts failed for WhatsApp number: {}", maxRetries, whatsappNumber);
        return false;
    }

    /**
     * Send WhatsApp message with custom template
     */
    public boolean sendTemplateMessage(String whatsappNumber, String templateName, 
                                     java.util.Map<String, String> parameters) {
        try {
            // TODO: Implement WhatsApp template message
            // Example implementation:
            /*
            WhatsAppTemplateRequest request = WhatsAppTemplateRequest.builder()
                .to(whatsappNumber)
                .template(templateName)
                .parameters(parameters)
                .build();
            
            WhatsAppResponse response = whatsAppApi.sendTemplate(phoneNumberId, request);
            return response.isSuccess();
            */
            
            log.info("Sending template message '{}' to {} with parameters: {}", 
                templateName, whatsappNumber, parameters);
            
            return sendMessage(whatsappNumber, "Template message: " + templateName);
            
        } catch (Exception e) {
            log.error("Error sending template message to {}: {}", whatsappNumber, e.getMessage());
            return false;
        }
    }
} 