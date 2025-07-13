package io.getarrays.securecapita.itinventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {

    boolean existsBySerialNumber(String serialNumber);

    List<Laptop> findByStatus(LaptopStatus status);
    
    List<Laptop> findByStatusAndStation(LaptopStatus status, String station);

    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.status = :status")
    long countByStatus(@Param("status")   LaptopStatus status);










    // Basic count methods (already available from JpaRepository)
    // long count(); // Total count of all laptops
    
    // Custom count methods based on actual entity fields

    
    long countByManufacturer(String manufacturer);
    
    long countBySerialNumberContaining(String serialNumber);
    
    // Count laptops by year
    @Query("SELECT COUNT(l) FROM Laptop l WHERE YEAR(l.purchaseDate) = :year")
    long countByPurchaseYear(@Param("year") int year);
    
    // Count laptops by issue year
    @Query("SELECT COUNT(l) FROM Laptop l WHERE YEAR(l.issueDate) = :year")
    long countByIssueYear(@Param("year") int year);
    
    // Count laptops by replacement year
    @Query("SELECT COUNT(l) FROM Laptop l WHERE YEAR(l.replacementDate) = :year")
    long countByReplacementYear(@Param("year") int year);
    
    // Count laptops by RAM size
    long countByRam(Integer ram);
    
    // Count laptops by processor
    long countByProcessor(Integer processor);
    
    // Count laptops by department
    long countByDepartment(String department);
    
    // Count laptops by designation
    long countByDesignation(String designation);
    
    // Count laptops issued to specific person
    long countByIssuedTo(String issuedTo);
    
    // Count laptops by email domain
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.email LIKE %:domain")
    long countByEmailDomain(@Param("domain") String domain);
    
    // Count laptops by age (older than specified years)
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.purchaseDate < :cutoffDate")
    long countOlderThan(@Param("cutoffDate") Date cutoffDate);
    
    // Count laptops that need replacement (past replacement date)
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.replacementDate <= :currentDate")
    long countNeedingReplacement();
    
    // Count laptops by RAM range
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.ram BETWEEN :minRam AND :maxRam")
    long countByRamRange(@Param("minRam") Integer minRam, @Param("maxRam") Integer maxRam);
    
    // Count laptops by processor range
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.processor BETWEEN :minProcessor AND :maxProcessor")
    long countByProcessorRange(@Param("minProcessor") Integer minProcessor, @Param("maxProcessor") Integer maxProcessor);
    
    // Count laptops by email containing specific text
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.email LIKE %:searchText%")
    long countByEmailContaining(@Param("searchText") String searchText);
    
    // Count laptops by serial number containing specific text
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.serialNumber LIKE %:searchText%")
    long countBySerialNumberContainingText(@Param("searchText") String searchText);
    
    // Count laptops by manufacturer containing specific text
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.manufacturer LIKE %:searchText%")
    long countByManufacturerContaining(@Param("searchText") String searchText);
    
    // Count laptops by department containing specific text
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.department LIKE %:searchText%")
    long countByDepartmentContaining(@Param("searchText") String searchText);
    
    // Count laptops by designation containing specific text
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.designation LIKE %:searchText%")
    long countByDesignationContaining(@Param("searchText") String searchText);
    
    // Count laptops by issuedTo containing specific text
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.issuedTo LIKE %:searchText%")
    long countByIssuedToContaining(@Param("searchText") String searchText);
    
    // Count laptops purchased in date range
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.purchaseDate BETWEEN :startDate AND :endDate")
    long countByPurchaseDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    // Count laptops issued in date range
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.issueDate BETWEEN :startDate AND :endDate")
    long countByIssueDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    // Count laptops with replacement date in range
    @Query("SELECT COUNT(l) FROM Laptop l WHERE l.replacementDate BETWEEN :startDate AND :endDate")
    long countByReplacementDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Optional<Laptop> findBySerialNumber(String serialNumber);
}
