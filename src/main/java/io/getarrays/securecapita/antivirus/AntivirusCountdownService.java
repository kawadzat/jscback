package io.getarrays.securecapita.antivirus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AntivirusCountdownService {

    /**
     * Get detailed countdown information for antivirus license expiration
     */
    public Map<String, Object> getDetailedCountdown(Antivirus antivirus) {
        Map<String, Object> countdown = new HashMap<>();
        
        if (antivirus.getLicenseExpirationDate() == null) {
            countdown.put("hasExpirationDate", false);
            countdown.put("message", "No expiration date set");
            return countdown;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationDate = antivirus.getLicenseExpirationDate();
        
        // Check if already expired
        if (now.isAfter(expirationDate)) {
            countdown.put("hasExpirationDate", true);
            countdown.put("isExpired", true);
            countdown.put("expiredDays", Math.abs(ChronoUnit.DAYS.between(now, expirationDate)));
            countdown.put("message", "License has expired");
            return countdown;
        }

        // Calculate time differences
        long days = ChronoUnit.DAYS.between(now, expirationDate);
        long hours = ChronoUnit.HOURS.between(now, expirationDate) % 24;
        long minutes = ChronoUnit.MINUTES.between(now, expirationDate) % 60;
        long seconds = ChronoUnit.SECONDS.between(now, expirationDate) % 60;

        // Determine urgency level
        String urgencyLevel = getUrgencyLevel(days);
        
        countdown.put("hasExpirationDate", true);
        countdown.put("isExpired", false);
        countdown.put("days", days);
        countdown.put("hours", hours);
        countdown.put("minutes", minutes);
        countdown.put("seconds", seconds);
        countdown.put("urgencyLevel", urgencyLevel);
        countdown.put("totalHours", ChronoUnit.HOURS.between(now, expirationDate));
        countdown.put("totalMinutes", ChronoUnit.MINUTES.between(now, expirationDate));
        countdown.put("totalSeconds", ChronoUnit.SECONDS.between(now, expirationDate));
        countdown.put("expirationDate", expirationDate);
        countdown.put("currentDate", now);
        
        // Add formatted countdown string
        countdown.put("formattedCountdown", formatCountdown(days, hours, minutes, seconds));
        
        return countdown;
    }

    /**
     * Get countdown for multiple antivirus entries
     */
    public Map<String, Object> getBulkCountdown(java.util.List<Antivirus> antivirusList) {
        Map<String, Object> bulkCountdown = new HashMap<>();
        
        int totalCount = antivirusList.size();
        int expiredCount = 0;
        int expiringSoonCount = 0; // Within 30 days
        int expiringVerySoonCount = 0; // Within 7 days
        int expiringCriticalCount = 0; // Within 24 hours
        
        java.util.List<Map<String, Object>> detailedCountdowns = new java.util.ArrayList<>();
        
        for (Antivirus antivirus : antivirusList) {
            Map<String, Object> countdown = getDetailedCountdown(antivirus);
            detailedCountdowns.add(countdown);
            
            if ((Boolean) countdown.get("isExpired")) {
                expiredCount++;
            } else {
                long days = (Long) countdown.get("days");
                if (days <= 1) {
                    expiringCriticalCount++;
                } else if (days <= 7) {
                    expiringVerySoonCount++;
                } else if (days <= 30) {
                    expiringSoonCount++;
                }
            }
        }
        
        bulkCountdown.put("totalCount", totalCount);
        bulkCountdown.put("expiredCount", expiredCount);
        bulkCountdown.put("expiringSoonCount", expiringSoonCount);
        bulkCountdown.put("expiringVerySoonCount", expiringVerySoonCount);
        bulkCountdown.put("expiringCriticalCount", expiringCriticalCount);
        bulkCountdown.put("detailedCountdowns", detailedCountdowns);
        
        return bulkCountdown;
    }

    /**
     * Get countdown statistics
     */
    public Map<String, Object> getCountdownStats(java.util.List<Antivirus> antivirusList) {
        Map<String, Object> stats = new HashMap<>();
        
        long totalExpiringIn24Hours = antivirusList.stream()
            .filter(av -> av.getLicenseExpirationDate() != null)
            .filter(av -> {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiration = av.getLicenseExpirationDate();
                return !now.isAfter(expiration) && 
                       ChronoUnit.HOURS.between(now, expiration) <= 24;
            })
            .count();
            
        long totalExpiringIn7Days = antivirusList.stream()
            .filter(av -> av.getLicenseExpirationDate() != null)
            .filter(av -> {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiration = av.getLicenseExpirationDate();
                return !now.isAfter(expiration) && 
                       ChronoUnit.DAYS.between(now, expiration) <= 7;
            })
            .count();
            
        long totalExpiringIn30Days = antivirusList.stream()
            .filter(av -> av.getLicenseExpirationDate() != null)
            .filter(av -> {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiration = av.getLicenseExpirationDate();
                return !now.isAfter(expiration) && 
                       ChronoUnit.DAYS.between(now, expiration) <= 30;
            })
            .count();
            
        long totalExpired = antivirusList.stream()
            .filter(av -> av.getLicenseExpirationDate() != null)
            .filter(av -> LocalDateTime.now().isAfter(av.getLicenseExpirationDate()))
            .count();
            
        long totalNoExpirationDate = antivirusList.stream()
            .filter(av -> av.getLicenseExpirationDate() == null)
            .count();
        
        stats.put("totalExpiringIn24Hours", totalExpiringIn24Hours);
        stats.put("totalExpiringIn7Days", totalExpiringIn7Days);
        stats.put("totalExpiringIn30Days", totalExpiringIn30Days);
        stats.put("totalExpired", totalExpired);
        stats.put("totalNoExpirationDate", totalNoExpirationDate);
        stats.put("totalWithExpirationDate", antivirusList.size() - totalNoExpirationDate);
        
        return stats;
    }

    /**
     * Determine urgency level based on days remaining
     */
    private String getUrgencyLevel(long days) {
        if (days <= 0) {
            return "EXPIRED";
        } else if (days <= 1) {
            return "CRITICAL";
        } else if (days <= 7) {
            return "HIGH";
        } else if (days <= 30) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    /**
     * Format countdown as a readable string
     */
    private String formatCountdown(long days, long hours, long minutes, long seconds) {
        if (days > 0) {
            return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d minutes, %d seconds", minutes, seconds);
        } else {
            return String.format("%d seconds", seconds);
        }
    }
} 