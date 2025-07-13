package io.getarrays.securecapita.antivirus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AntivirusRepository extends JpaRepository<Antivirus, Long> {

    // Check if antivirus exists by key
    Optional<Antivirus> findByKey(String key);
    
    // Check if antivirus exists by key (boolean)
    boolean existsByKey(String key);
    
    // Find antivirus by key and laptop ID
    Optional<Antivirus> findByKeyAndLaptopId(String key, Long laptopId);
    
    // Check if antivirus exists by key and laptop ID
    boolean existsByKeyAndLaptopId(String key, Long laptopId);
    
    // Find all antivirus by laptop ID
    List<Antivirus> findByLaptopId(Long laptopId);
    
    // Find antivirus by name
    List<Antivirus> findByNameContainingIgnoreCase(String name);
    
    // Find antivirus by vendor
    List<Antivirus> findByVendor(String vendor);
    
    // Find antivirus by status
    List<Antivirus> findByStatus(AntivirusStatus status);
    
    // Find installed antivirus
    List<Antivirus> findByIsInstalled(Boolean isInstalled);
    
    // Find expiring licenses
    @Query("SELECT a FROM Antivirus a WHERE a.licenseExpirationDate <= :date")
    List<Antivirus> findExpiringLicenses(@Param("date") LocalDateTime date);
} 