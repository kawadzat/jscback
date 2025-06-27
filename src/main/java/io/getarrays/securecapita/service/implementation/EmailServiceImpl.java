package io.getarrays.securecapita.service.implementation;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import io.getarrays.securecapita.enumeration.VerificationType;
import io.getarrays.securecapita.exception.ApiException;
import io.getarrays.securecapita.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 5/27/2023
 */

@Service
@AllArgsConstructor
@Slf4j


public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    private static final String fromEmail = "zimjsc2020@gmail.com";
    @Override
    public void sendVerificationEmail(String firstName, String email, String key, String verificationUrl, VerificationType verificationType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true to set content type to HTML

            helper.setFrom(fromEmail);
            helper.setTo(email);

            String emailContent = getEmailMessage(firstName, verificationUrl, key, verificationType);
            helper.setText(emailContent, true); // true to interpret the email content as HTML

            helper.setSubject(String.format("JSC - %s Verification Email", StringUtils.capitalize(verificationType.getType())));

            mailSender.send(message);
            log.info("Email sent to {}", firstName);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    @Override
    public void sendEmail(String email, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true to set content type to HTML

            helper.setFrom(fromEmail);
            helper.setTo(email);

            helper.setText(content, true); // true to interpret the email content as HTML

            helper.setSubject(subject);

            mailSender.send(message);
            log.info("Email sent to {}", email);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }


    private String getEmailMessage(String firstName, String verificationUrl, String key, VerificationType verificationType) {
        StringBuilder emailBody = new StringBuilder("<html><head><style>");
        emailBody.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }")
                .append(".card { background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 600px; margin: auto; padding: 20px; }")
                .append("a { display: inline-block; background-color: #4CAF50; color:  #000000; text-decoration: none; padding: 12px 24px; border-radius: 4px; font-size: 16px; }")
                .append("a:hover { background-color: #45a049; }")
                .append("</style></head><body>");

        emailBody.append("<div class='card'>")
                .append("<h2 style='margin-bottom: 20px;'>Hello ").append(firstName).append(",</h2>")
                .append("<p>Your verification key:</p>")
                .append("<pre>").append(key).append("</pre>");

        switch (verificationType) {
            case PASSWORD:
                emailBody.append("<p>Reset password request. Please click the button below to reset your password.</p>")
                        .append("<a href='").append(verificationUrl).append("' style='display: block; margin-top: 20px;'>Reset Password</a>")
                        .append("<p>The Support Team</p>");
                break;
            case ACCOUNT:
                emailBody.append("<p>Hello ").append(firstName).append(",</p>")
                        .append("<p>Your new account has been created. Please click the button below to verify your account.</p>")
                        .append("<a href='").append(verificationUrl).append("' style='display: block; margin-top: 20px;'>Verify Account</a>")
                        .append("<p>The Support Team</p>");
                break;
            default:
                throw new ApiException("Unable to send email. Email type unknown");
        }

        emailBody.append("</div></body></html>");

        return emailBody.toString();
    }

}



