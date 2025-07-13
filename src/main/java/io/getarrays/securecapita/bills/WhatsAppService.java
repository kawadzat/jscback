package io.getarrays.securecapita.bills;

public interface WhatsAppService {
    
    /**
     * Send a water bill message to a WhatsApp number
     * @param whatsappNumber The WhatsApp number to send to
     * @param message The formatted message to send
     * @return true if sent successfully, false otherwise
     */
    boolean sendMessage(String whatsappNumber, String message);
    
    /**
     * Send a water bill to a WhatsApp number
     * @param waterBill The water bill to send
     * @return true if sent successfully, false otherwise
     */
    boolean sendWaterBill(WaterBill waterBill);
    
    /**
     * Send bulk water bills to multiple WhatsApp numbers
     * @param waterBills List of water bills to send
     * @return Number of successfully sent messages
     */
    int sendBulkWaterBills(java.util.List<WaterBill> waterBills);
    
    /**
     * Check if a WhatsApp number is valid
     * @param whatsappNumber The WhatsApp number to validate
     * @return true if valid, false otherwise
     */
    boolean isValidWhatsAppNumber(String whatsappNumber);
    
    /**
     * Get delivery status for a sent message
     * @param messageId The message ID
     * @return Delivery status
     */
    String getDeliveryStatus(String messageId);
    
    /**
     * Check if WhatsApp service is available
     * @return true if available, false otherwise
     */
    boolean isServiceAvailable();
} 