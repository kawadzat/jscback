package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.antivirus.Antivirus;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface LicenceInterface {
    Licence addLicenceToLaptop(Long laptopId, Licence licence);

    List<Licence> getAll();
    
    // Count methods
    long getTotalCount();

    // Get comprehensive count statistics
    Map<String, Object> getCountStatistics();
    
    // Additional count methods for LicenceController
    long getCountByStatus(LicenseStatus status);
    long getCountByLicenseType(LicenseType licenseType);
    long getCountBySupplier(String supplier);
    long getCountByVendor(String vendor);
    long getCountByDepartment(String department);
    long getCountByStation(String station);
    long getCountAssignedLicenses();
    long getCountUnassignedLicenses();
    long getCountExpiredLicenses(Date currentDate);
    long getCountExpiringLicenses(Date startDate, Date endDate);
    long getCountByPriceRange(Double minPrice, Double maxPrice);
    long getCountByPurchaseYear(int year);
    long getCountByInstallationYear(int year);
    long getCountByExpiryYear(int year);
    long getCountByCurrency(String currency);
    long getCountByVersion(String version);
    long getCountBySeatsRange(Integer minSeats, Integer maxSeats);
    long getCountByLicenseNameContaining(String searchText);
    long getCountByDescriptionContaining(String searchText);
    long getCountByNotesContaining(String searchText);
}
