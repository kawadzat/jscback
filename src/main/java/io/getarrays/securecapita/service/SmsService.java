package io.getarrays.securecapita.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {
    
    public void sendSMS(String phoneNumber, String message) {
        // In a real implementation, this would integrate with Twilio, AWS SNS, or similar
        log.info("📱 SMS sent to {}: {}", phoneNumber, message);
        
        // Example Twilio integration:
        // Twilio.init(accountSid, authToken);
        // Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(fromNumber), message).create();
    }
} 