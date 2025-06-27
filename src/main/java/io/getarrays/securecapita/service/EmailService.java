package io.getarrays.securecapita.service;

import io.getarrays.securecapita.enumeration.VerificationType;

public interface EmailService {
    void sendVerificationEmail(String firstName, String email, String key, String verificationUrl, VerificationType verificationType);
    void sendEmail(String email, String subject, String content);

}
