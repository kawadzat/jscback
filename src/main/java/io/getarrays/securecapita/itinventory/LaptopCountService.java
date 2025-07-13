package io.getarrays.securecapita.itinventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LaptopCountService {

    private final LaptopRepository laptopRepository;

    /**
     * Get total count of all laptops
     */
    public long getTotalCount() {
        return laptopRepository.count();
    }

    /**
     * Get count by status
     */
    public long getCountByStatus(LaptopStatus status) {
        return laptopRepository.countByStatus(status);
    }

    /**
     * Get count by manufacturer
     */
    public long getCountByManufacturer(String manufacturer) {
        return laptopRepository.countByManufacturer(manufacturer);
    }

    /**
     * Get count by department
     */
    public long getCountByDepartment(String department) {
        return laptopRepository.countByDepartment(department);
    }

    /**
     * Get count by designation
     */
    public long getCountByDesignation(String designation) {
        return laptopRepository.countByDesignation(designation);
    }

    /**
     * Get count by RAM size
     */
    public long getCountByRam(Integer ram) {
        return laptopRepository.countByRam(ram);
    }

    /**
     * Get count by processor
     */
    public long getCountByProcessor(Integer processor) {
        return laptopRepository.countByProcessor(processor);
    }

    /**
     * Get count by purchase year
     */
    public long getCountByPurchaseYear(int year) {
        return laptopRepository.countByPurchaseYear(year);
    }

    /**
     * Get count by issue year
     */
    public long getCountByIssueYear(int year) {
        return laptopRepository.countByIssueYear(year);
    }

    /**
     * Get count by replacement year
     */
    public long getCountByReplacementYear(int year) {
        return laptopRepository.countByReplacementYear(year);
    }

    /**
     * Get count of laptops needing replacement
     */
    public long getCountNeedingReplacement() {
        return laptopRepository.countNeedingReplacement();
    }

    /**
     * Get count by RAM range
     */
    public long getCountByRamRange(Integer minRam, Integer maxRam) {
        return laptopRepository.countByRamRange(minRam, maxRam);
    }

    /**
     * Get count by processor range
     */
    public long getCountByProcessorRange(Integer minProcessor, Integer maxProcessor) {
        return laptopRepository.countByProcessorRange(minProcessor, maxProcessor);
    }

    /**
     * Get count by email domain
     */
    public long getCountByEmailDomain(String domain) {
        return laptopRepository.countByEmailDomain(domain);
    }

    /**
     * Get count by purchase date range
     */
    public long getCountByPurchaseDateRange(Date startDate, Date endDate) {
        return laptopRepository.countByPurchaseDateRange(startDate, endDate);
    }

    /**
     * Get count by issue date range
     */
    public long getCountByIssueDateRange(Date startDate, Date endDate) {
        return laptopRepository.countByIssueDateRange(startDate, endDate);
    }

    /**
     * Get count by replacement date range
     */
    public long getCountByReplacementDateRange(Date startDate, Date endDate) {
        return laptopRepository.countByReplacementDateRange(startDate, endDate);
    }

    /**
     * Get count by email containing text
     */
    public long getCountByEmailContaining(String searchText) {
        return laptopRepository.countByEmailContaining(searchText);
    }

    /**
     * Get count by serial number containing text
     */
    public long getCountBySerialNumberContaining(String searchText) {
        return laptopRepository.countBySerialNumberContainingText(searchText);
    }

    /**
     * Get count by manufacturer containing text
     */
    public long getCountByManufacturerContaining(String searchText) {
        return laptopRepository.countByManufacturerContaining(searchText);
    }

    /**
     * Get count by department containing text
     */
    public long getCountByDepartmentContaining(String searchText) {
        return laptopRepository.countByDepartmentContaining(searchText);
    }

    /**
     * Get count by designation containing text
     */
    public long getCountByDesignationContaining(String searchText) {
        return laptopRepository.countByDesignationContaining(searchText);
    }

    /**
     * Get count by issuedTo containing text
     */
    public long getCountByIssuedToContaining(String searchText) {
        return laptopRepository.countByIssuedToContaining(searchText);
    }

    /**
     * Get count of laptops older than specified date
     */
    public long getCountOlderThan(Date cutoffDate) {
        return laptopRepository.countOlderThan(cutoffDate);
    }

    /**
     * Get comprehensive statistics
     */
    public Map<String, Object> getComprehensiveStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total count
        stats.put("totalLaptops", getTotalCount());
        
        // Count by status
        for (LaptopStatus status : LaptopStatus.values()) {
            stats.put("status_" + status.name().toLowerCase(), getCountByStatus(status));
        }
        
        // Count needing replacement
        stats.put("needingReplacement", getCountNeedingReplacement());
        
        // Count by year (current year and previous 2 years)
        int currentYear = new Date().getYear() + 1900;
        for (int year = currentYear - 2; year <= currentYear; year++) {
            stats.put("purchased_" + year, getCountByPurchaseYear(year));
            stats.put("issued_" + year, getCountByIssueYear(year));
            stats.put("replacement_" + year, getCountByReplacementYear(year));
        }
        
        return stats;
    }

    /**
     * Get department statistics
     */
    public Map<String, Long> getDepartmentStats() {
        // This would need to be implemented with a custom query to get all departments
        // For now, return empty map - can be extended later
        return new HashMap<>();
    }

    /**
     * Get manufacturer statistics
     */
    public Map<String, Long> getManufacturerStats() {
        // This would need to be implemented with a custom query to get all manufacturers
        // For now, return empty map - can be extended later
        return new HashMap<>();
    }
} 