package io.getarrays.securecapita.antivirus;

import io.getarrays.securecapita.maintenance.Maintenance;

import java.time.LocalDateTime;
import java.util.List;

public interface AntivirusService {
    Antivirus addAntivirusToLaptop(Long laptopId, Antivirus antivirus);
    
    // Duplicate prevention methods
    boolean isKeyExists(String key);
    boolean isKeyExistsForLaptop(String key, Long laptopId);
    Antivirus addAntivirusToLaptopWithDuplicateCheck(Long laptopId, Antivirus antivirus);
    Antivirus updateAntivirusWithDuplicateCheck(Long laptopId, Long antivirusId, Antivirus antivirus);
    
    // Basic CRUD operations
    Antivirus create(Antivirus antivirus);
    Antivirus update(Antivirus antivirus);
    Antivirus getById(Long id);
    Antivirus getByKey(String key);
    List<Antivirus> getAll();
    void delete(Long id);
    
    // Laptop-related methods
    List<Antivirus> getAntivirusByLaptop(Long laptopId);
    void removeAntivirusFromLaptop(Long laptopId, Long antivirusId);
    Antivirus updateAntivirusOnLaptop(Long laptopId, Antivirus antivirus);
    
    // Search and filter methods
    List<Antivirus> getByName(String name);
    List<Antivirus> getByVendor(String vendor);
    List<Antivirus> getByStatus(AntivirusStatus status);
    List<Antivirus> getInstalledAntivirus();
    List<Antivirus> getExpiringLicenses(LocalDateTime date);
    
    // DTO conversion methods
    AntivirusDto convertToDto(Antivirus antivirus);
    List<AntivirusDto> getAntivirusDtosByLaptop(Long laptopId);
    
    // Search by laptop serial number
    List<Antivirus> getByLaptopSerialNumber(String serialNumber);
    List<AntivirusDto> getAntivirusDtosByLaptopSerialNumber(String serialNumber);
}