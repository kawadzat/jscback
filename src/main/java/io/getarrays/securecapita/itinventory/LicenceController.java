package io.getarrays.securecapita.itinventory;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing software licenses and license-laptop assignments
 */
@RestController
@RequestMapping("/licence")
@AllArgsConstructor
@Slf4j
public class LicenceController {

    private final LicenceService licenceService;


//    @PostMapping("/create/assign/laptop/{laptopId}")
//    public ResponseEntity<LicenceDto> createAndAssignLicence(@PathVariable Long laptopId, @RequestBody @Valid LicenceDto licenceDto) {
//        UserDTO currentUser = UserUtils.getAuthenticatedUser(SecurityContextHolder.getContext().getAuthentication());
//        LicenceDto createdLicence = licenceService.createAndAssignLicence(currentUser, licenceDto, laptopId);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdLicence);
//    }

    @PostMapping("/create/assign/laptop/{laptopId}")
    public ResponseEntity<Licence> addLicenceToLaptop(@PathVariable Long laptopId, @RequestBody Licence licence) {
        return new ResponseEntity<>(licenceService.addLicenceToLaptop(laptopId, licence), HttpStatus.CREATED);
    }



    @GetMapping("/all-with-detailed-laptop-info")
    public ResponseEntity<List<LicenceWithDetailedLaptopDto>> getAllLicenceWithDetailedLaptopInfo() {
        List<Licence> licenceList = licenceService.getAll();
        List<LicenceWithDetailedLaptopDto> result = licenceList.stream()
                .map(this::convertToDetailedDto)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(result);
    }


    private LicenceWithDetailedLaptopDto convertToDetailedDto(Licence licence) {
        LicenceWithDetailedLaptopDto dto = new LicenceWithDetailedLaptopDto();
        dto.setId(licence.getId());
        dto.setAssetType(licence.getAssetType());
        dto.setLicenseName(licence.getLicenseName());
        dto.setDescription(licence.getDescription());
        dto.setLicenseKey(licence.getLicenseKey());
        dto.setPurchaseDate(licence.getPurchaseDate());
        dto.setInstallationDate(licence.getInstallationDate());
        dto.setExpiryDate(licence.getExpiryDate());
        dto.setLicenseType(licence.getLicenseType());
        dto.setStatus(licence.getStatus());
        dto.setSupplier(licence.getSupplier());
        dto.setVendor(licence.getVendor());
        dto.setNumberOfSeats(licence.getNumberOfSeats());
        dto.setPrice(licence.getPrice());
        dto.setCurrency(licence.getCurrency());
        dto.setVersion(licence.getVersion());
        dto.setInstallationPath(licence.getInstallationPath());
        dto.setFilePath(licence.getFilePath());
        dto.setDaysToExpiration(licence.getDaysToExpiration());
        dto.setDepartment(licence.getDepartment());
        dto.setStation(licence.getStation());
        dto.setNotes(licence.getNotes());

        // Map laptop information if available
        if (licence.getLaptop() != null) {
            dto.setLaptopId(licence.getLaptop().getId());
            dto.setLaptopSerialNumber(licence.getLaptop().getSerialNumber());
            dto.setLaptopManufacturer(licence.getLaptop().getManufacturer());
            dto.setLaptopAssertType(licence.getLaptop().getAssertType());
        }

        return dto;
    }

    // Count endpoints
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount() {
        return ResponseEntity.ok(licenceService.getTotalCount());
    }

    /**
     * Get total lines count (total number of licence records)
     */
    @GetMapping("/count/totalLines")
    public ResponseEntity<Map<String, Long>> getTotalLines() {
        long totalLines = licenceService.getTotalCount();
        return ResponseEntity.ok(Map.of("totalLines", totalLines));
    }

    /**
     * Get comprehensive count statistics
     */
    @GetMapping("/count/statistics")
    public ResponseEntity<Map<String, Object>> getCountStatistics() {
        Map<String, Object> stats = licenceService.getCountStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get count by status
     */
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Map<String, Object>> getCountByStatus(@PathVariable LicenseStatus status) {
        long count = licenceService.getCountByStatus(status);
        return ResponseEntity.ok(Map.of(
            "status", status.name(),
            "count", count
        ));
    }

    /**
     * Get count by license type
     */
    @GetMapping("/count/license-type/{licenseType}")
    public ResponseEntity<Map<String, Object>> getCountByLicenseType(@PathVariable LicenseType licenseType) {
        long count = licenceService.getCountByLicenseType(licenseType);
        return ResponseEntity.ok(Map.of(
            "licenseType", licenseType.name(),
            "count", count
        ));
    }

    /**
     * Get count by supplier
     */
    @GetMapping("/count/supplier/{supplier}")
    public ResponseEntity<Map<String, Object>> getCountBySupplier(@PathVariable String supplier) {
        long count = licenceService.getCountBySupplier(supplier);
        return ResponseEntity.ok(Map.of(
            "supplier", supplier,
            "count", count
        ));
    }

    /**
     * Get count by vendor
     */
    @GetMapping("/count/vendor/{vendor}")
    public ResponseEntity<Map<String, Object>> getCountByVendor(@PathVariable String vendor) {
        long count = licenceService.getCountByVendor(vendor);
        return ResponseEntity.ok(Map.of(
            "vendor", vendor,
            "count", count
        ));
    }

    /**
     * Get count by department
     */
    @GetMapping("/count/department/{department}")
    public ResponseEntity<Map<String, Object>> getCountByDepartment(@PathVariable String department) {
        long count = licenceService.getCountByDepartment(department);
        return ResponseEntity.ok(Map.of(
            "department", department,
            "count", count
        ));
    }

    /**
     * Get count by station
     */
    @GetMapping("/count/station/{station}")
    public ResponseEntity<Map<String, Object>> getCountByStation(@PathVariable String station) {
        long count = licenceService.getCountByStation(station);
        return ResponseEntity.ok(Map.of(
            "station", station,
            "count", count
        ));
    }

    /**
     * Get count of assigned licenses
     */
    @GetMapping("/count/assigned")
    public ResponseEntity<Map<String, Long>> getCountAssignedLicenses() {
        long count = licenceService.getCountAssignedLicenses();
        return ResponseEntity.ok(Map.of("assignedLicenses", count));
    }

    /**
     * Get count of unassigned licenses
     */
    @GetMapping("/count/unassigned")
    public ResponseEntity<Map<String, Long>> getCountUnassignedLicenses() {
        long count = licenceService.getCountUnassignedLicenses();
        return ResponseEntity.ok(Map.of("unassignedLicenses", count));
    }

    /**
     * Get count of expired licenses
     */
    @GetMapping("/count/expired")
    public ResponseEntity<Map<String, Long>> getCountExpiredLicenses() {
        long count = licenceService.getCountExpiredLicenses(new Date());
        return ResponseEntity.ok(Map.of("expiredLicenses", count));
    }

    /**
     * Get count by purchase year
     */
    @GetMapping("/count/purchase-year/{year}")
    public ResponseEntity<Map<String, Object>> getCountByPurchaseYear(@PathVariable int year) {
        long count = licenceService.getCountByPurchaseYear(year);
        return ResponseEntity.ok(Map.of(
            "purchaseYear", year,
            "count", count
        ));
    }

    /**
     * Get count by currency
     */
    @GetMapping("/count/currency/{currency}")
    public ResponseEntity<Map<String, Object>> getCountByCurrency(@PathVariable String currency) {
        long count = licenceService.getCountByCurrency(currency);
        return ResponseEntity.ok(Map.of(
            "currency", currency,
            "count", count
        ));
    }

    /**
     * Get count by version
     */
    @GetMapping("/count/version/{version}")
    public ResponseEntity<Map<String, Object>> getCountByVersion(@PathVariable String version) {
        long count = licenceService.getCountByVersion(version);
        return ResponseEntity.ok(Map.of(
            "version", version,
            "count", count
        ));
    }


} 