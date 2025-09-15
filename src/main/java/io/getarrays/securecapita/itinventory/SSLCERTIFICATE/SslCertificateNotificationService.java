package io.getarrays.securecapita.itinventory.SSLCERTIFICATE;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class SslCertificateNotificationService {

    private final SslCerticateRepository sslCerticateRepository;
    private final UserRepository1 userRepository1;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void checkSslCertificateExpiryAndNotify() {
        log.info("Starting SSL certificate expiry notification check");
        
        try {
            LocalDate twoMonthsFromNow = LocalDate.now().plus(2, ChronoUnit.MONTHS);
            Date twoMonthsFromNowDate = Date.from(twoMonthsFromNow.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            List<SslCerticate> expiringCertificates = findCertificatesExpiringInTwoMonths(twoMonthsFromNowDate);
            
            if (!expiringCertificates.isEmpty()) {
                log.info("Found {} SSL certificates expiring in 2 months", expiringCertificates.size());
                sendExpiryNotifications(expiringCertificates);
            } else {
                log.info("No SSL certificates expiring in 2 months found");
            }
            
        } catch (Exception e) {
            log.error("Error checking SSL certificate expiry notifications: {}", e.getMessage(), e);
        }
        
        log.info("Completed SSL certificate expiry notification check");
    }

    private List<SslCerticate> findCertificatesExpiringInTwoMonths(Date targetDate) {
        LocalDate targetLocalDate = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startDate = targetLocalDate.minusDays(7);
        LocalDate endDate = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(7);
        
        Date startDateObj = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDateObj = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        return sslCerticateRepository.findAll().stream()
                .filter(cert -> cert.getExpiryDate() != null)
                .filter(cert -> {
                    Date expiryDate = cert.getExpiryDate();
                    return expiryDate.after(startDateObj) && expiryDate.before(endDateObj);
                })
                .collect(Collectors.toList());
    }

    private void sendExpiryNotifications(List<SslCerticate> expiringCertificates) {
        List<User> cyberSecuritySpecialists = userRepository1.findByRoleName("CYBER_SECURITY_SPECIALIST");
        List<User> systemAdmins = userRepository1.findByRoleName("SYSADMIN");
        
        List<User> allRecipients = new java.util.ArrayList<>();
        allRecipients.addAll(cyberSecuritySpecialists);
        allRecipients.addAll(systemAdmins);
        
        if (allRecipients.isEmpty()) {
            log.warn("No cyber security specialists or system admins found to notify");
            return;
        }
        
        for (User recipient : allRecipients) {
            try {
                sendExpiryNotification(recipient, expiringCertificates);
                log.info("Sent SSL certificate expiry notification to: {}", recipient.getEmail());
            } catch (Exception e) {
                log.error("Failed to send SSL certificate expiry notification to {}: {}", 
                    recipient.getEmail(), e.getMessage());
            }
        }
    }

    private void sendExpiryNotification(User recipient, List<SslCerticate> expiringCertificates) {
        String subject = "SSL Certificate Expiry Alert - Action Required";
        String content = buildExpiryNotificationEmail(recipient, expiringCertificates);
        
        emailService.sendEmail(recipient.getEmail(), subject, content);
    }

    private String buildExpiryNotificationEmail(User recipient, List<SslCerticate> expiringCertificates) {
        StringBuilder emailContent = new StringBuilder();
        
        emailContent.append("<html><head><style>");
        emailContent.append("body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f5f5f5; }");
        emailContent.append(".card { background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 800px; margin: auto; padding: 20px; }");
        emailContent.append(".header { background-color: #ff6b6b; color: white; padding: 15px; border-radius: 5px; margin-bottom: 20px; }");
        emailContent.append(".certificate-table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        emailContent.append(".certificate-table th, .certificate-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        emailContent.append(".certificate-table th { background-color: #f2f2f2; font-weight: bold; }");
        emailContent.append(".urgent { background-color: #fff3cd; border-left: 4px solid #ffc107; }");
        emailContent.append(".warning { background-color: #d1ecf1; border-left: 4px solid #17a2b8; }");
        emailContent.append("</style></head><body>");
        
        emailContent.append("<div class='card'>");
        emailContent.append("<div class='header'>");
        emailContent.append("<h2>🚨 SSL Certificate Expiry Alert</h2>");
        emailContent.append("</div>");
        
        emailContent.append("<p>Dear <strong>").append(recipient.getFirstName()).append(" ").append(recipient.getLastName()).append("</strong>,</p>");
        
        emailContent.append("<p>This is an automated alert regarding SSL certificates that are scheduled to expire in approximately <strong>2 months</strong>.</p>");
        
        emailContent.append("<p><strong>Action Required:</strong> Please review the following certificates and take necessary actions to renew them before expiration to avoid service disruptions.</p>");
        
        emailContent.append("<table class='certificate-table'>");
        emailContent.append("<thead><tr>");
        emailContent.append("<th>Domain Name</th>");
        emailContent.append("<th>Certificate Serial</th>");
        emailContent.append("<th>Vendor</th>");
        emailContent.append("<th>Expiry Date</th>");
        emailContent.append("<th>Days Until Expiry</th>");
        emailContent.append("<th>Category</th>");
        emailContent.append("</tr></thead><tbody>");
        
        for (SslCerticate cert : expiringCertificates) {
            long daysUntilExpiry = calculateDaysUntilExpiry(cert.getExpiryDate());
            String rowClass = daysUntilExpiry <= 30 ? "urgent" : "warning";
            
            emailContent.append("<tr class='").append(rowClass).append("'>");
            emailContent.append("<td><strong>").append(cert.getDomainName() != null ? cert.getDomainName() : "N/A").append("</strong></td>");
            emailContent.append("<td>").append(cert.getCertificateSerialNumber() != null ? cert.getCertificateSerialNumber() : "N/A").append("</td>");
            emailContent.append("<td>").append(cert.getVendor() != null ? cert.getVendor() : "N/A").append("</td>");
            emailContent.append("<td>").append(formatDate(cert.getExpiryDate())).append("</td>");
            emailContent.append("<td><strong>").append(daysUntilExpiry).append(" days</strong></td>");

            emailContent.append("</tr>");
        }
        
        emailContent.append("</tbody></table>");
        
        emailContent.append("<div style='margin-top: 20px; padding: 15px; background-color: #f8f9fa; border-radius: 5px;'>");
        emailContent.append("<h3>📋 Recommended Actions:</h3>");
        emailContent.append("<ul>");
        emailContent.append("<li>Contact the certificate vendor/supplier to initiate renewal process</li>");
        emailContent.append("<li>Verify domain ownership and validation requirements</li>");
        emailContent.append("<li>Schedule certificate replacement during maintenance windows</li>");
        emailContent.append("<li>Update internal documentation and inventory records</li>");
        emailContent.append("<li>Test new certificates in staging environment before production deployment</li>");
        emailContent.append("</ul>");
        emailContent.append("</div>");
        
        emailContent.append("<div style='margin-top: 20px; padding: 15px; background-color: #fff3cd; border-radius: 5px;'>");
        emailContent.append("<p><strong>⚠️ Important:</strong> SSL certificate expiration can cause website downtime, security warnings, and service disruptions. Please prioritize these renewals.</p>");
        emailContent.append("</div>");
        
        emailContent.append("<p style='margin-top: 20px;'>If you have any questions or need assistance with the renewal process, please contact the IT team.</p>");
        
        emailContent.append("<p>Best regards,<br><strong>JSC IT Security Team</strong></p>");
        
        emailContent.append("<div style='margin-top: 20px; font-size: 12px; color: #666;'>");
        emailContent.append("<p>This is an automated notification. Please do not reply to this email.</p>");
        emailContent.append("</div>");
        
        emailContent.append("</div></body></html>");
        
        return emailContent.toString();
    }

    private long calculateDaysUntilExpiry(Date expiryDate) {
        if (expiryDate == null) return 0;
        
        LocalDate expiryLocalDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        
        return ChronoUnit.DAYS.between(currentDate, expiryLocalDate);
    }

    private String formatDate(Date date) {
        if (date == null) return "N/A";
        
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.toString();
    }

    public void manuallyCheckAndNotify() {
        log.info("Manual SSL certificate expiry check triggered");
        checkSslCertificateExpiryAndNotify();
    }

    public long getCountOfCertificatesExpiringInTwoMonths() {
        LocalDate twoMonthsFromNow = LocalDate.now().plus(2, ChronoUnit.MONTHS);
        Date twoMonthsFromNowDate = Date.from(twoMonthsFromNow.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        List<SslCerticate> expiringCertificates = findCertificatesExpiringInTwoMonths(twoMonthsFromNowDate);
        return expiringCertificates.size();
    }
}
