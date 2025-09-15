package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.antivirus.Antivirus;
import io.getarrays.securecapita.antivirus.AntivirusStatus;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing software licenses
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class LicenceService implements LicenceInterface {

    private final LicenceRepository licenceRepository;
    private final UserRepository<User> userRepository;
    private final LaptopRepository laptopRepository;

    @Override
    public Licence addLicenceToLaptop(Long laptopId, Licence licence) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));

        // Check if antivirus with this key already exists
        if (licenceRepository.existsByLicenseKey(licence.getLicenseKey())) {
            throw new IllegalArgumentException("Licence with key '" + licence.getLicenseKey() + "' already exists");
        }

       licence.setLaptop(laptop);

        return licenceRepository.save(licence);
    }

    @Override
    public List<Licence> getAll() {
        return licenceRepository.findAll();
    }
    
    // Count method implementations
    @Override
    public long getTotalCount() {
        return licenceRepository.count();
    }

    @Override
    public Map<String, Object> getCountStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total count
        stats.put("totalLicenses", getTotalCount());
        
        // Count by status
        for (LicenseStatus status : LicenseStatus.values()) {
            stats.put("status_" + status.name().toLowerCase(), getCountByStatus(status));
        }
        
        // Count by license type
        for (LicenseType type : LicenseType.values()) {
            stats.put("type_" + type.name().toLowerCase(), getCountByLicenseType(type));
        }
        
        // Count assigned vs unassigned
        stats.put("assignedLicenses", getCountAssignedLicenses());
        stats.put("unassignedLicenses", getCountUnassignedLicenses());
        
        // Count expired licenses
        stats.put("expiredLicenses", getCountExpiredLicenses(new Date()));
        
        return stats;
    }
    
    // Additional count method implementations
    @Override
    public long getCountByStatus(LicenseStatus status) {
        return licenceRepository.countByStatus(status);
    }
    
    @Override
    public long getCountByLicenseType(LicenseType licenseType) {
        return licenceRepository.countByLicenseType(licenseType);
    }
    
    @Override
    public long getCountBySupplier(String supplier) {
        return licenceRepository.countBySupplier(supplier);
    }
    
    @Override
    public long getCountByVendor(String vendor) {
        return licenceRepository.countByVendor(vendor);
    }
    
    @Override
    public long getCountByDepartment(String department) {
        return licenceRepository.countByDepartment(department);
    }
    
    @Override
    public long getCountByStation(String station) {
        return licenceRepository.countByStation(station);
    }
    
    @Override
    public long getCountAssignedLicenses() {
        return licenceRepository.countAssignedLicenses();
    }
    
    @Override
    public long getCountUnassignedLicenses() {
        return licenceRepository.countUnassignedLicenses();
    }
    
    @Override
    public long getCountExpiredLicenses(Date currentDate) {
        return licenceRepository.countExpiredLicenses(currentDate);
    }
    
    @Override
    public long getCountExpiringLicenses(Date startDate, Date endDate) {
        return licenceRepository.countExpiringLicenses(startDate, endDate);
    }
    
    @Override
    public long getCountByPriceRange(Double minPrice, Double maxPrice) {
        return licenceRepository.countByPriceRange(minPrice, maxPrice);
    }
    
    @Override
    public long getCountByPurchaseYear(int year) {
        return licenceRepository.countByPurchaseYear(year);
    }
    
    @Override
    public long getCountByInstallationYear(int year) {
        return licenceRepository.countByInstallationYear(year);
    }
    
    @Override
    public long getCountByExpiryYear(int year) {
        return licenceRepository.countByExpiryYear(year);
    }
    
    @Override
    public long getCountByCurrency(String currency) {
        return licenceRepository.countByCurrency(currency);
    }
    
    @Override
    public long getCountByVersion(String version) {
        return licenceRepository.countByVersion(version);
    }
    
    @Override
    public long getCountBySeatsRange(Integer minSeats, Integer maxSeats) {
        return licenceRepository.countBySeatsRange(minSeats, maxSeats);
    }
    
    @Override
    public long getCountByLicenseNameContaining(String searchText) {
        return licenceRepository.countByLicenseNameContaining(searchText);
    }
    
    @Override
    public long getCountByDescriptionContaining(String searchText) {
        return licenceRepository.countByDescriptionContaining(searchText);
    }
    
    @Override
    public long getCountByNotesContaining(String searchText) {
        return licenceRepository.countByNotesContaining(searchText);
    }
}