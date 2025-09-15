package io.getarrays.securecapita.itinventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Licence entity
 */
@Repository
public interface LicenceRepository extends JpaRepository<Licence, Long> {
    boolean existsByLicenseKey(String licenseKey);
    
    // Count methods for LicenceController
    long countByStatus(LicenseStatus status);
    long countByLicenseType(LicenseType licenseType);
    long countBySupplier(String supplier);
    long countByVendor(String vendor);
    long countByDepartment(String department);
    long countByStation(String station);
    
    // Count assigned vs unassigned licenses
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.laptop IS NOT NULL")
    long countAssignedLicenses();
    
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.laptop IS NULL")
    long countUnassignedLicenses();
    
    // Count expired licenses
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.expiryDate <= :currentDate")
    long countExpiredLicenses(@Param("currentDate") Date currentDate);
    
    // Count expiring licenses within date range
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.expiryDate BETWEEN :startDate AND :endDate")
    long countExpiringLicenses(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    // Count by price range
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.price BETWEEN :minPrice AND :maxPrice")
    long countByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    // Count by purchase year
    @Query("SELECT COUNT(l) FROM Licence l WHERE YEAR(l.purchaseDate) = :year")
    long countByPurchaseYear(@Param("year") int year);
    
    // Count by installation year
    @Query("SELECT COUNT(l) FROM Licence l WHERE YEAR(l.installationDate) = :year")
    long countByInstallationYear(@Param("year") int year);
    
    // Count by expiry year
    @Query("SELECT COUNT(l) FROM Licence l WHERE YEAR(l.expiryDate) = :year")
    long countByExpiryYear(@Param("year") int year);
    
    // Count by currency
    long countByCurrency(String currency);
    
    // Count by version
    long countByVersion(String version);
    
    // Count by seats range
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.numberOfSeats BETWEEN :minSeats AND :maxSeats")
    long countBySeatsRange(@Param("minSeats") Integer minSeats, @Param("maxSeats") Integer maxSeats);
    
    // Count by license name containing text
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.licenseName LIKE %:searchText%")
    long countByLicenseNameContaining(@Param("searchText") String searchText);
    
    // Count by description containing text
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.description LIKE %:searchText%")
    long countByDescriptionContaining(@Param("searchText") String searchText);
    
    // Count by notes containing text
    @Query("SELECT COUNT(l) FROM Licence l WHERE l.notes LIKE %:searchText%")
    long countByNotesContaining(@Param("searchText") String searchText);
} 