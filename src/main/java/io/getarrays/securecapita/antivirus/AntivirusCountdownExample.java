package io.getarrays.securecapita.antivirus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Example class demonstrating how to use the Antivirus Countdown functionality
 * This class shows various ways to work with countdown data
 */
@Component
@RequiredArgsConstructor
public class AntivirusCountdownExample {

    private final AntivirusCountdownService countdownService;
    private final AntivirusCountdownMapper countdownMapper;

    /**
     * Example: Get detailed countdown for a single antivirus
     */
    public void demonstrateSingleCountdown(Antivirus antivirus) {
        System.out.println("=== Single Antivirus Countdown Example ===");
        
        Map<String, Object> countdown = countdownService.getDetailedCountdown(antivirus);
        
        System.out.println("Antivirus: " + antivirus.getName());
        System.out.println("Vendor: " + antivirus.getVendor());
        System.out.println("Expiration Date: " + antivirus.getLicenseExpirationDate());
        System.out.println("Is Expired: " + countdown.get("isExpired"));
        System.out.println("Urgency Level: " + countdown.get("urgencyLevel"));
        System.out.println("Formatted Countdown: " + countdown.get("formattedCountdown"));
        System.out.println("Days Remaining: " + countdown.get("days"));
        System.out.println("Hours Remaining: " + countdown.get("hours"));
        System.out.println("Minutes Remaining: " + countdown.get("minutes"));
        System.out.println("Seconds Remaining: " + countdown.get("seconds"));
        System.out.println("Total Seconds: " + countdown.get("totalSeconds"));
        System.out.println();
    }

    /**
     * Example: Get countdown statistics for multiple antivirus entries
     */
    public void demonstrateBulkCountdown(List<Antivirus> antivirusList) {
        System.out.println("=== Bulk Countdown Statistics Example ===");
        
        Map<String, Object> bulkCountdown = countdownService.getBulkCountdown(antivirusList);
        Map<String, Object> stats = countdownService.getCountdownStats(antivirusList);
        
        System.out.println("Total Antivirus Count: " + bulkCountdown.get("totalCount"));
        System.out.println("Expired Count: " + bulkCountdown.get("expiredCount"));
        System.out.println("Expiring Soon Count: " + bulkCountdown.get("expiringSoonCount"));
        System.out.println("Expiring Very Soon Count: " + bulkCountdown.get("expiringVerySoonCount"));
        System.out.println("Expiring Critical Count: " + bulkCountdown.get("expiringCriticalCount"));
        System.out.println();
        
        System.out.println("=== Detailed Statistics ===");
        System.out.println("Expiring in 24 hours: " + stats.get("totalExpiringIn24Hours"));
        System.out.println("Expiring in 7 days: " + stats.get("totalExpiringIn7Days"));
        System.out.println("Expiring in 30 days: " + stats.get("totalExpiringIn30Days"));
        System.out.println("Total expired: " + stats.get("totalExpired"));
        System.out.println("No expiration date: " + stats.get("totalNoExpirationDate"));
        System.out.println();
    }

    /**
     * Example: Convert to DTO with countdown information
     */
    public void demonstrateDtoConversion(List<Antivirus> antivirusList) {
        System.out.println("=== DTO Conversion Example ===");
        
        List<AntivirusCountdownDto> countdownDtos = countdownMapper.toCountdownDtoList(antivirusList);
        
        for (AntivirusCountdownDto dto : countdownDtos) {
            System.out.println("Antivirus ID: " + dto.getAntivirusId());
            System.out.println("Name: " + dto.getAntivirusName());
            System.out.println("Urgency Level: " + dto.getUrgencyLevel());
            System.out.println("Formatted Countdown: " + dto.getFormattedCountdown());
            System.out.println("Laptop User: " + dto.getLaptopUser());
            System.out.println("---");
        }
        System.out.println();
    }

    /**
     * Example: Create sample antivirus data for testing
     */
    public Antivirus createSampleAntivirus() {
        return Antivirus.builder()
            .id(1L)
            .name("Norton Antivirus")
            .key("NORTON-2024-001")
            .vendor("Norton")
            .version("2024.1.0")
            .status(AntivirusStatus.ACTIVE)
            .isInstalled(true)
            .licenseExpirationDate(LocalDateTime.now().plusDays(5)) // Expires in 5 days
            .build();
    }

    /**
     * Example: Create expired antivirus data for testing
     */
    public Antivirus createExpiredAntivirus() {
        return Antivirus.builder()
            .id(2L)
            .name("McAfee Antivirus")
            .key("MCAFEE-2024-002")
            .vendor("McAfee")
            .version("2024.2.0")
            .status(AntivirusStatus.ACTIVE)
            .isInstalled(true)
            .licenseExpirationDate(LocalDateTime.now().minusDays(3)) // Expired 3 days ago
            .build();
    }

    /**
     * Example: Create urgent antivirus data for testing
     */
    public Antivirus createUrgentAntivirus() {
        return Antivirus.builder()
            .id(3L)
            .name("Kaspersky Antivirus")
            .key("KASPERSKY-2024-003")
            .vendor("Kaspersky")
            .version("2024.3.0")
            .status(AntivirusStatus.ACTIVE)
            .isInstalled(true)
            .licenseExpirationDate(LocalDateTime.now().plusHours(12)) // Expires in 12 hours
            .build();
    }

    /**
     * Example: Run all demonstrations
     */
    public void runAllExamples() {
        System.out.println("Running Antivirus Countdown Examples...\n");
        
        // Create sample data
        Antivirus sample1 = createSampleAntivirus();
        Antivirus sample2 = createExpiredAntivirus();
        Antivirus sample3 = createUrgentAntivirus();
        
        List<Antivirus> sampleList = List.of(sample1, sample2, sample3);
        
        // Run demonstrations
        demonstrateSingleCountdown(sample1);
        demonstrateSingleCountdown(sample2);
        demonstrateSingleCountdown(sample3);
        demonstrateBulkCountdown(sampleList);
        demonstrateDtoConversion(sampleList);
        
        System.out.println("Examples completed!");
    }
} 