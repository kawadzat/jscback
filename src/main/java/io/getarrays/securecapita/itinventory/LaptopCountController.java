 package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import io.getarrays.securecapita.antivirus.AntivirusRepository;
import io.getarrays.securecapita.itinventory.LaptopRepository;
import io.getarrays.securecapita.itinventory.LaptopStatus;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/laptop-counts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LaptopCountController {

    private final LaptopCountService laptopCountService;

    @Autowired
    private AntivirusRepository antivirusRepository;
    @Autowired
    private LaptopRepository laptopRepository;

    /**
     * Get total count of all laptops
     */
    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> getTotalCount() {
        long count = laptopCountService.getTotalCount();
        return ResponseEntity.ok(Map.of("totalLaptops", count));
    }

    /**
     * Get total count of all laptops
     */
    @GetMapping("/total-laptops")
    public ResponseEntity<Map<String, Long>> getTotalLaptops() {
        long totalLaptops = laptopRepository.count();
        Map<String, Long> result = new java.util.HashMap<>();
        result.put("totalLaptops", totalLaptops);
        return ResponseEntity.ok(result);
    }

    /**
     * Get count by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getCountByStatus(@PathVariable LaptopStatus status) {
        long count = laptopCountService.getCountByStatus(status);
        return ResponseEntity.ok(Map.of(
            "status", status.name(),
            "count", count
        ));
    }

    /**
     * Get count by manufacturer
     */
    @GetMapping("/manufacturer/{manufacturer}")
    public ResponseEntity<Map<String, Object>> getCountByManufacturer(@PathVariable String manufacturer) {
        long count = laptopCountService.getCountByManufacturer(manufacturer);
        return ResponseEntity.ok(Map.of(
            "manufacturer", manufacturer,
            "count", count
        ));
    }

    /**
     * Get count by department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<Map<String, Object>> getCountByDepartment(@PathVariable String department) {
        long count = laptopCountService.getCountByDepartment(department);
        return ResponseEntity.ok(Map.of(
            "department", department,
            "count", count
        ));
    }

    /**
     * Get count by designation
     */
    @GetMapping("/designation/{designation}")
    public ResponseEntity<Map<String, Object>> getCountByDesignation(@PathVariable String designation) {
        long count = laptopCountService.getCountByDesignation(designation);
        return ResponseEntity.ok(Map.of(
            "designation", designation,
            "count", count
        ));
    }

    /**
     * Get count by RAM size
     */
    @GetMapping("/ram/{ram}")
    public ResponseEntity<Map<String, Object>> getCountByRam(@PathVariable Integer ram) {
        long count = laptopCountService.getCountByRam(ram);
        return ResponseEntity.ok(Map.of(
            "ram", ram,
            "count", count
        ));
    }

    /**
     * Get count by processor
     */
    @GetMapping("/processor/{processor}")
    public ResponseEntity<Map<String, Object>> getCountByProcessor(@PathVariable Integer processor) {
        long count = laptopCountService.getCountByProcessor(processor);
        return ResponseEntity.ok(Map.of(
            "processor", processor,
            "count", count
        ));
    }

    /**
     * Get count by purchase year
     */
    @GetMapping("/purchase-year/{year}")
    public ResponseEntity<Map<String, Object>> getCountByPurchaseYear(@PathVariable int year) {
        long count = laptopCountService.getCountByPurchaseYear(year);
        return ResponseEntity.ok(Map.of(
            "purchaseYear", year,
            "count", count
        ));
    }

    /**
     * Get count by issue year
     */
    @GetMapping("/issue-year/{year}")
    public ResponseEntity<Map<String, Object>> getCountByIssueYear(@PathVariable int year) {
        long count = laptopCountService.getCountByIssueYear(year);
        return ResponseEntity.ok(Map.of(
            "issueYear", year,
            "count", count
        ));
    }

    /**
     * Get count by replacement year
     */
    @GetMapping("/replacement-year/{year}")
    public ResponseEntity<Map<String, Object>> getCountByReplacementYear(@PathVariable int year) {
        long count = laptopCountService.getCountByReplacementYear(year);
        return ResponseEntity.ok(Map.of(
            "replacementYear", year,
            "count", count
        ));
    }

    /**
     * Get count of laptops needing replacement
     */
    @GetMapping("/needing-replacement")
    public ResponseEntity<Map<String, Long>> getCountNeedingReplacement() {
        long count = laptopCountService.getCountNeedingReplacement();
        return ResponseEntity.ok(Map.of("needingReplacement", count));
    }

    /**
     * Get count by RAM range
     */
    @GetMapping("/ram-range")
    public ResponseEntity<Map<String, Object>> getCountByRamRange(
            @RequestParam Integer minRam,
            @RequestParam Integer maxRam) {
        long count = laptopCountService.getCountByRamRange(minRam, maxRam);
        return ResponseEntity.ok(Map.of(
            "minRam", minRam,
            "maxRam", maxRam,
            "count", count
        ));
    }

    /**
     * Get count by processor range
     */
    @GetMapping("/processor-range")
    public ResponseEntity<Map<String, Object>> getCountByProcessorRange(
            @RequestParam Integer minProcessor,
            @RequestParam Integer maxProcessor) {
        long count = laptopCountService.getCountByProcessorRange(minProcessor, maxProcessor);
        return ResponseEntity.ok(Map.of(
            "minProcessor", minProcessor,
            "maxProcessor", maxProcessor,
            "count", count
        ));
    }

    /**
     * Get count by email domain
     */
    @GetMapping("/email-domain/{domain}")
    public ResponseEntity<Map<String, Object>> getCountByEmailDomain(@PathVariable String domain) {
        long count = laptopCountService.getCountByEmailDomain(domain);
        return ResponseEntity.ok(Map.of(
            "emailDomain", domain,
            "count", count
        ));
    }

    /**
     * Get count by purchase date range
     */
    @GetMapping("/purchase-date-range")
    public ResponseEntity<Map<String, Object>> getCountByPurchaseDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        long count = laptopCountService.getCountByPurchaseDateRange(startDate, endDate);
        return ResponseEntity.ok(Map.of(
            "startDate", startDate,
            "endDate", endDate,
            "count", count
        ));
    }

    /**
     * Get count by issue date range
     */
    @GetMapping("/issue-date-range")
    public ResponseEntity<Map<String, Object>> getCountByIssueDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        long count = laptopCountService.getCountByIssueDateRange(startDate, endDate);
        return ResponseEntity.ok(Map.of(
            "startDate", startDate,
            "endDate", endDate,
            "count", count
        ));
    }

    /**
     * Get count by replacement date range
     */
    @GetMapping("/replacement-date-range")
    public ResponseEntity<Map<String, Object>> getCountByReplacementDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        long count = laptopCountService.getCountByReplacementDateRange(startDate, endDate);
        return ResponseEntity.ok(Map.of(
            "startDate", startDate,
            "endDate", endDate,
            "count", count
        ));
    }

    /**
     * Get count by email containing text
     */
    @GetMapping("/email-contains")
    public ResponseEntity<Map<String, Object>> getCountByEmailContaining(@RequestParam String searchText) {
        long count = laptopCountService.getCountByEmailContaining(searchText);
        return ResponseEntity.ok(Map.of(
            "searchText", searchText,
            "count", count
        ));
    }

    /**
     * Get count by serial number containing text
     */
    @GetMapping("/serial-number-contains")
    public ResponseEntity<Map<String, Object>> getCountBySerialNumberContaining(@RequestParam String searchText) {
        long count = laptopCountService.getCountBySerialNumberContaining(searchText);
        return ResponseEntity.ok(Map.of(
            "searchText", searchText,
            "count", count
        ));
    }

    /**
     * Get count by manufacturer containing text
     */
    @GetMapping("/manufacturer-contains")
    public ResponseEntity<Map<String, Object>> getCountByManufacturerContaining(@RequestParam String searchText) {
        long count = laptopCountService.getCountByManufacturerContaining(searchText);
        return ResponseEntity.ok(Map.of(
            "searchText", searchText,
            "count", count
        ));
    }

    /**
     * Get count by department containing text
     */
    @GetMapping("/department-contains")
    public ResponseEntity<Map<String, Object>> getCountByDepartmentContaining(@RequestParam String searchText) {
        long count = laptopCountService.getCountByDepartmentContaining(searchText);
        return ResponseEntity.ok(Map.of(
            "searchText", searchText,
            "count", count
        ));
    }

    /**
     * Get count by designation containing text
     */
    @GetMapping("/designation-contains")
    public ResponseEntity<Map<String, Object>> getCountByDesignationContaining(@RequestParam String searchText) {
        long count = laptopCountService.getCountByDesignationContaining(searchText);
        return ResponseEntity.ok(Map.of(
            "searchText", searchText,
            "count", count
        ));
    }

    /**
     * Get count by issuedTo containing text
     */
    @GetMapping("/issued-to-contains")
    public ResponseEntity<Map<String, Object>> getCountByIssuedToContaining(@RequestParam String searchText) {
        long count = laptopCountService.getCountByIssuedToContaining(searchText);
        return ResponseEntity.ok(Map.of(
            "searchText", searchText,
            "count", count
        ));
    }

    /**
     * Get count of laptops older than specified date
     */
    @GetMapping("/older-than")
    public ResponseEntity<Map<String, Object>> getCountOlderThan(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date cutoffDate) {
        long count = laptopCountService.getCountOlderThan(cutoffDate);
        return ResponseEntity.ok(Map.of(
            "cutoffDate", cutoffDate,
            "count", count
        ));
    }

    /**
     * Get comprehensive statistics
     */
    @GetMapping("/comprehensive-stats")
    public ResponseEntity<Map<String, Object>> getComprehensiveStats() {
        Map<String, Object> stats = laptopCountService.getComprehensiveStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get total antivirus, issued laptops count, and available laptops count
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getSummaryCounts() {
        long totalAntivirus = antivirusRepository.count();
        long issuedLaptopsCount = laptopRepository.countByStatus(LaptopStatus.ISSUED);
        long availableLaptopsCount = laptopRepository.countByStatus(LaptopStatus.AVAILABLE);
        Map<String, Long> result = new java.util.HashMap<>();
        result.put("totalAntivirus", totalAntivirus);
        result.put("issuedLaptopsCount", issuedLaptopsCount);
        result.put("availableLaptopsCount", availableLaptopsCount);
        return ResponseEntity.ok(result);
    }

    /**
     * Get total antivirus count only
     */
    @GetMapping("/total-antivirus")
    public ResponseEntity<Map<String, Long>> getTotalAntivirus() {
        long totalAntivirus = antivirusRepository.count();
        Map<String, Long> result = new java.util.HashMap<>();
        result.put("totalAntivirus", totalAntivirus);
        return ResponseEntity.ok(result);
    }
} 