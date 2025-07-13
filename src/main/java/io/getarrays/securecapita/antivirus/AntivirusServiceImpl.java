package io.getarrays.securecapita.antivirus;

import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.itinventory.Laptop;
import io.getarrays.securecapita.itinventory.LaptopRepository;
import io.getarrays.securecapita.maintenance.Maintenance;
import io.getarrays.securecapita.maintenance.MaintenancePriority;
import io.getarrays.securecapita.maintenance.MaintenanceRepository;
import io.getarrays.securecapita.maintenance.MaintenanceStatus;
import io.getarrays.securecapita.maintenance.MaintenanceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AntivirusServiceImpl implements AntivirusService {

    private final AntivirusRepository antivirusRepository;
    private final LaptopRepository laptopRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Override
    public Antivirus addAntivirusToLaptop(Long laptopId, Antivirus antivirus) {
        Laptop laptop = laptopRepository.findById(laptopId)
            .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));

        // Check if antivirus with this key already exists
        if (antivirusRepository.existsByKey(antivirus.getKey())) {
            throw new IllegalArgumentException("Antivirus with key '" + antivirus.getKey() + "' already exists");
        }

        antivirus.setLaptop(laptop);

        if (antivirus.getStatus() == null) {
            antivirus.setStatus(AntivirusStatus.ACTIVE);
        }

        if (antivirus.getIsInstalled() == null) {
            antivirus.setIsInstalled(true);
        }

        return antivirusRepository.save(antivirus);
    }

    public Maintenance addMaintenanceToLaptop(Long laptopId, Maintenance maintenance) {
        Laptop laptop = laptopRepository.findById(laptopId)
                .orElseThrow(() -> new ResourceNotFoundException("Laptop not found with id: " + laptopId));

        maintenance.setLaptop(laptop);

        // Set default maintenance type if not provided
        if (maintenance.getMaintenanceType() == null) {
            maintenance.setMaintenanceType(MaintenanceType.PREVENTIVE);
        }

        // Set default description if not provided
        if (maintenance.getDescription() == null || maintenance.getDescription().trim().isEmpty()) {
            maintenance.setDescription("Scheduled maintenance for laptop " + laptopId);
        }

        // Set default scheduled date if not provided
        if (maintenance.getScheduledDate() == null) {
            maintenance.setScheduledDate(LocalDateTime.now().plusDays(1)); // Default to tomorrow
        }

        // Only set status to SCHEDULED if it's not already set
        if (maintenance.getStatus() == null) {
            maintenance.setStatus(MaintenanceStatus.SCHEDULED);
        }

        // Set default priority if not provided
        if (maintenance.getPriority() == null) {
            maintenance.setPriority(MaintenancePriority.MEDIUM);
        }

        return maintenanceRepository.save(maintenance);
    }

    @Override
    public List<Antivirus> getAll() {
        return antivirusRepository.findAll();
    }

    @Override
    public Antivirus getById(Long id) {
        return antivirusRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Antivirus not found with id: " + id));
    }

    // Check if antivirus key exists
    public boolean isKeyExists(String key) {
        return antivirusRepository.existsByKey(key);
    }

    // Get antivirus by key
    public Antivirus getByKey(String key) {
        return antivirusRepository.findByKey(key)
            .orElseThrow(() -> new ResourceNotFoundException("Antivirus not found with key: " + key));
    }

    // Add antivirus with duplicate check
    public Antivirus addAntivirusWithDuplicateCheck(Long laptopId, Antivirus antivirus) {
        // First check if key already exists
        if (antivirusRepository.existsByKey(antivirus.getKey())) {
            throw new IllegalArgumentException("Antivirus with key '" + antivirus.getKey() + "' already exists. Please use a different key.");
        }
        
        return addAntivirusToLaptop(laptopId, antivirus);
    }

    // Update antivirus with duplicate check
    public Antivirus updateAntivirusWithDuplicateCheck(Long antivirusId, Antivirus antivirus) {
        Antivirus existingAntivirus = antivirusRepository.findById(antivirusId)
            .orElseThrow(() -> new ResourceNotFoundException("Antivirus not found with id: " + antivirusId));
        
        // Check if the new key conflicts with another antivirus
        if (!existingAntivirus.getKey().equals(antivirus.getKey()) && 
            antivirusRepository.existsByKey(antivirus.getKey())) {
            throw new IllegalArgumentException("Antivirus with key '" + antivirus.getKey() + "' already exists. Please use a different key.");
        }
        
        // Update fields
        existingAntivirus.setName(antivirus.getName());
        existingAntivirus.setKey(antivirus.getKey());
        existingAntivirus.setRenewTimeInterval(antivirus.getRenewTimeInterval());
        existingAntivirus.setVersion(antivirus.getVersion());
        existingAntivirus.setVendor(antivirus.getVendor());
        existingAntivirus.setStatus(antivirus.getStatus());
        existingAntivirus.setIsInstalled(antivirus.getIsInstalled());
        existingAntivirus.setLicenseExpirationDate(antivirus.getLicenseExpirationDate());
        
        return antivirusRepository.save(existingAntivirus);
    }

    // Check if antivirus key exists for specific laptop
    public boolean isKeyExistsForLaptop(String key, Long laptopId) {
        return antivirusRepository.existsByKeyAndLaptopId(key, laptopId);
    }

    // Add antivirus to laptop with duplicate check
    public Antivirus addAntivirusToLaptopWithDuplicateCheck(Long laptopId, Antivirus antivirus) {
        return addAntivirusWithDuplicateCheck(laptopId, antivirus);
    }

    // Update antivirus with duplicate check (laptop-specific)
    public Antivirus updateAntivirusWithDuplicateCheck(Long laptopId, Long antivirusId, Antivirus antivirus) {
        return updateAntivirusWithDuplicateCheck(antivirusId, antivirus);
    }

    // Basic CRUD operations
    public Antivirus create(Antivirus antivirus) {
        if (antivirusRepository.existsByKey(antivirus.getKey())) {
            throw new IllegalArgumentException("Antivirus with key '" + antivirus.getKey() + "' already exists");
        }
        return antivirusRepository.save(antivirus);
    }

    public Antivirus update(Antivirus antivirus) {
        if (!antivirusRepository.existsById(antivirus.getId())) {
            throw new ResourceNotFoundException("Antivirus not found with id: " + antivirus.getId());
        }
        
        Antivirus existing = antivirusRepository.findById(antivirus.getId()).get();
        if (!existing.getKey().equals(antivirus.getKey()) && 
            antivirusRepository.existsByKey(antivirus.getKey())) {
            throw new IllegalArgumentException("Antivirus with key '" + antivirus.getKey() + "' already exists");
        }
        
        return antivirusRepository.save(antivirus);
    }

    public void delete(Long id) {
        if (!antivirusRepository.existsById(id)) {
            throw new ResourceNotFoundException("Antivirus not found with id: " + id);
        }
        antivirusRepository.deleteById(id);
    }

    // Laptop-related methods
    public List<Antivirus> getAntivirusByLaptop(Long laptopId) {
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        return antivirusRepository.findByLaptopId(laptopId);
    }

    public void removeAntivirusFromLaptop(Long laptopId, Long antivirusId) {
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        
        Antivirus antivirus = antivirusRepository.findById(antivirusId)
            .orElseThrow(() -> new ResourceNotFoundException("Antivirus not found with id: " + antivirusId));
        
        if (!antivirus.getLaptop().getId().equals(laptopId)) {
            throw new IllegalArgumentException("Antivirus does not belong to the specified laptop");
        }
        
        antivirusRepository.delete(antivirus);
    }

    public Antivirus updateAntivirusOnLaptop(Long laptopId, Antivirus antivirus) {
        if (!laptopRepository.existsById(laptopId)) {
            throw new ResourceNotFoundException("Laptop not found with id: " + laptopId);
        }
        
        Antivirus existingAntivirus = antivirusRepository.findById(antivirus.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Antivirus not found with id: " + antivirus.getId()));
        
        if (!existingAntivirus.getLaptop().getId().equals(laptopId)) {
            throw new IllegalArgumentException("Antivirus does not belong to the specified laptop");
        }
        
        if (!existingAntivirus.getKey().equals(antivirus.getKey()) && 
            antivirusRepository.existsByKey(antivirus.getKey())) {
            throw new IllegalArgumentException("Antivirus with key '" + antivirus.getKey() + "' already exists");
        }
        
        // Update fields but keep the laptop relationship
        existingAntivirus.setName(antivirus.getName());
        existingAntivirus.setKey(antivirus.getKey());
        existingAntivirus.setRenewTimeInterval(antivirus.getRenewTimeInterval());
        existingAntivirus.setVersion(antivirus.getVersion());
        existingAntivirus.setVendor(antivirus.getVendor());
        existingAntivirus.setStatus(antivirus.getStatus());
        existingAntivirus.setIsInstalled(antivirus.getIsInstalled());
        existingAntivirus.setLicenseExpirationDate(antivirus.getLicenseExpirationDate());
        
        return antivirusRepository.save(existingAntivirus);
    }

    // Search and filter methods
    public List<Antivirus> getByName(String name) {
        return antivirusRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Antivirus> getByVendor(String vendor) {
        return antivirusRepository.findByVendor(vendor);
    }

    public List<Antivirus> getByStatus(AntivirusStatus status) {
        return antivirusRepository.findByStatus(status);
    }

    public List<Antivirus> getInstalledAntivirus() {
        return antivirusRepository.findByIsInstalled(true);
    }

    public List<Antivirus> getExpiringLicenses(LocalDateTime date) {
        return antivirusRepository.findExpiringLicenses(date);
    }
    
    // Convert Antivirus entity to DTO
    public AntivirusDto convertToDto(Antivirus antivirus) {
        return AntivirusDto.builder()
                .id(antivirus.getId())
                .name(antivirus.getName())
                .key(antivirus.getKey())
                .renewTimeInterval(antivirus.getRenewTimeInterval())
                .version(antivirus.getVersion())
                .vendor(antivirus.getVendor())
                .status(antivirus.getStatus())
                .isInstalled(antivirus.getIsInstalled())
                .licenseExpirationDate(antivirus.getLicenseExpirationDate())
                .daysToExpiration(antivirus.getDaysToExpiration())
                .build();
    }
    
    // Get antivirus DTOs by laptop ID
    public List<AntivirusDto> getAntivirusDtosByLaptop(Long laptopId) {
        List<Antivirus> antivirusList = getAntivirusByLaptop(laptopId);
        return antivirusList.stream()
                .map(this::convertToDto)
                .collect(java.util.stream.Collectors.toList());
    }
}