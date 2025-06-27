package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.dto.AssertDto;
import io.getarrays.securecapita.asserts.dto.AssertsResponseDto;
import io.getarrays.securecapita.asserts.dto.StationAssertsDto;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.asserts.model.SpecificationInput;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.repo.AssertEntityRepository;
import io.getarrays.securecapita.asserts.repo.AssertsJpaRepository;
import io.getarrays.securecapita.asserts.repo.InspectionRepository;
import io.getarrays.securecapita.asserts.repo.StationRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.*;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.jasper.downloadtoken.DownloadToken;
import io.getarrays.securecapita.jasper.downloadtoken.DownloadTokenRepository;
import io.getarrays.securecapita.jasper.downloadtoken.DownloadTokenService;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import io.getarrays.securecapita.officelocations.OfficeLocationRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import io.getarrays.securecapita.userlogs.ActionType;
import io.getarrays.securecapita.userlogs.UserLogService;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AssertService implements AssertServiceInterface {

    private final AssertEntityRepository assertRepository;
    private final InspectionRepository inspectionRepository;
    private final StationRepository stationRepository;
    private final AssertEntityRepository assertEntityRepository;
    private final AssertsJpaRepository assertsJpaRepository;
    private final UserLogService userLogService;
    private final UserRepository1 userRepository1;
    private final OfficeLocationRepository officeLocationRepository;






    public long countAssertsForUser(Long userId) {
        User user = userRepository1.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return assertRepository.countAsserts(user);}




    public long ccountAssertsForUser(Long userId) {
        User user = userRepository1.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return assertRepository.ccountAsserts(user);}












    public List<AssertEntity> findAllMovableAssets() {
        return assertRepository.findAllMovableAssets();
    }




    

    public AssertEntity updateAssertEntity(AssertEntity assertEntity) {
        AssertEntity updatedAssertEntity = assertRepository.save(assertEntity);
        userLogService.addLog(ActionType.UPDATED, "updated assert successfully.");
        return updatedAssertEntity;
    }

    public ResponseEntity<?> updateAssertEntity(Long assertId, AssertDto dto) throws Exception {
        AssertEntity assertEntity = assertEntityRepository.findById(assertId)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid assert id"));

        Optional<AssertEntity> duplicatedAssert = assertsJpaRepository.findByAssetAndStation(dto.getAssetNumber(), dto.getSelectedStationID());
        if (duplicatedAssert.isPresent() && assertId!=duplicatedAssert.get().getId()) {
            return ResponseEntity.status(422).body(new CustomMessage("Found Duplicate Entry. Please check again."));
        }
        Optional<Station> optionalStation = stationRepository.findById(dto.getSelectedStationID());
        if (optionalStation.isEmpty()) {
            throw new ResourceNotFoundException("Station not found");
        }
        Optional<OfficeLocation> optionalOfficeLocation = officeLocationRepository.findByStationAndName(optionalStation.get().getStation_id(), dto.getLocation());
        if (optionalOfficeLocation.isEmpty()) {
            throw new ResourceNotFoundException("Office Location not found");
        }
        assertEntity.setOfficeLocation(optionalOfficeLocation.get());
        assertEntity.setStation(optionalStation.get());
        assertEntity.setDate(dto.getDate());
        assertEntity.setAssetDisc(dto.getAssetDisc());
        assertEntity.setMovable(dto.getMovable());
        assertEntity.setLocation(dto.getLocation());
        assertEntity.setAssetNumber(dto.getAssetNumber());
        assertEntity.setAssertType(dto.getAssertType());
        assertEntity.setInitialRemarks(dto.getInitialRemarks());
        assertEntity.setSerialNumber(dto.getSerialNumber());
        assertEntity.setInvoiceNumber(dto.getInvoiceNumber());
        assertEntity.setQuantity(dto.getQuantity());

        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        assertEntity.setPreparedBy(user);
        AssertEntity createdAssert = assertRepository.save(assertEntity);
        userLogService.addLog(ActionType.UPDATED, "updated assert successfully.");
        return ResponseEntity.ok(createdAssert);
    }

    /* to create user */
    public ResponseEntity<?> createAssert(AssertDto assertDto) throws Exception {
        //check duplicate
        Optional<AssertEntity> duplicatedAssert = assertsJpaRepository.findByAssetAndStation(assertDto.getAssetNumber(), assertDto.getSelectedStationID());
        if (duplicatedAssert.isPresent()) {
            return ResponseEntity.status(422).body(new CustomMessage("Found Duplicate Entry. Please check again."));
        }
        Optional<Station> optionalStation = stationRepository.findById(assertDto.getSelectedStationID());
        if (optionalStation.isEmpty()) {
            throw new ResourceNotFoundException("Station not found");
        }
        Optional<OfficeLocation> optionalOfficeLocation = officeLocationRepository.findByStationAndName(optionalStation.get().getStation_id(), assertDto.getLocation());
        if (optionalOfficeLocation.isEmpty()) {
            throw new ResourceNotFoundException("Office Location not found");
        }
        AssertEntity newAssert = new AssertEntity();
        newAssert.setOfficeLocation(optionalOfficeLocation.get());
        newAssert.setStation(optionalStation.get());
        newAssert.setDate(assertDto.getDate());
        newAssert.setAssetDisc(assertDto.getAssetDisc());
        newAssert.setMovable(assertDto.getMovable());
        newAssert.setLocation(assertDto.getLocation());
        newAssert.setAssetNumber(assertDto.getAssetNumber());
        newAssert.setAssertType(assertDto.getAssertType());
        newAssert.setInitialRemarks(assertDto.getInitialRemarks());
        newAssert.setSerialNumber(assertDto.getSerialNumber());
        newAssert.setInvoiceNumber(assertDto.getInvoiceNumber());
        newAssert.setQuantity(assertDto.getQuantity());
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        newAssert.setPreparedBy(user);
        AssertEntity createdAssert = assertRepository.save(newAssert);
        userLogService.addLog(ActionType.CREATED, "created assert successfully. Assert: " + newAssert.getAssetDisc());
        return ResponseEntity.ok(createdAssert);
    }


//    @Override
//    public void addInspectionToAseertEnity(Long id, Inspection inspection) {
//        AssertEntity assertEntity = assertRepository.findById(id).orElse(null);
//
//        if (assertEntity != null) {
//            inspection.getAssertEntity(assertEntity);
//            inspectionRepository.save(inspection);
//        }
//    }

    @Override
    public void addInspectionToAssertEntity(Long id, Inspection inspection) {
        Optional<AssertEntity> assertEntityOptional = assertRepository.findByAssetId(id);

        if (assertEntityOptional.isPresent()) {
            AssertEntity assertEntity = assertEntityOptional.get();
            inspection.setAssertEntity(assertEntity);
            inspectionRepository.save(inspection);
        }
        userLogService.addLog(ActionType.UPDATED, "added inspection to assert.");

    }

    public Inspection getInspection(Long id) {
        return inspectionRepository.findById(id).get();
    }

    @Override
    public AssertEntity getAssertEntityById(Long assertEntityId) {
        return null;
    }

    public List<AssertEntity> getAllAssertEntities() {
        return assertEntityRepository.findAll();}






        public Page<AssertEntity> getAsserts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("lastModifiedDate").descending());

        return assertRepository.findAll(pageRequest);
    }

    @Override
    public ResponseEntity<?> getAllAssertsByStation(Long userId, Long stationId, String query, PageRequest pageRequest) {
        return ResponseEntity.ok(assertRepository.getAllAssertsByStationPage(stationId, query, pageRequest));
    }






    @Override
    public ResponseEntity<?> getAllAssertsByUserStation(Long userId, Long stationId, String query, PageRequest pageRequest) {
        return ResponseEntity.ok(assertRepository.getAssertsByUserStationPaged(userId, stationId, query, pageRequest));
    }

    @Override
    public ResponseEntity<?> getAllAssertsByUserStation(Long userId, String query, PageRequest pageRequest) {
        User user = userRepository1.findById(userId).get();
        if (!user.isStationAssigned()) {
            return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to get asserts for any station."));
        }
        return ResponseEntity.ok(assertRepository.getAssertsByUserStationPaged(userId, user.getStations().stream().findFirst().get().getId(), query, pageRequest));
    }

    public List<AssertEntity> getAllAssertsByUserStation(Long stationId, String query) {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!user.isStationAssigned(stationId)&& authentication.getAuthorities().stream().noneMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            throw new RuntimeException("You are not authorized to get asserts for any station.");
        }else if(authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))){
                if(query == null||query.isEmpty()) {
                    return assertRepository.getAssertsByStation(stationId);
                }
            return assertRepository.getAssertsByStation(stationId, query);
        }
        if(query == null||query.isEmpty()) {
            return assertRepository.getAssertsByUserStation(user.getId(), stationId);
        }
        return assertRepository.getAssertsByUserStation(user.getId(), stationId, query);
    }

    @Override
    public ResponseEntity<?> getStats() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            // Fetch the total fixed asserts and total current asserts
            long totalFixedAsserts = assertsJpaRepository.countFixedAsserts();
            long totalCurrentAsserts = assertsJpaRepository.countCurrentAsserts();
            long totalMovableAsserts=assertsJpaRepository.countMovableAsserts();

            // Fetch asset statistics
            ArrayList<AssetItemStat> assetsStats = assertsJpaRepository.findAssertItemStatsByAssetDisc();
            userLogService.addLog(ActionType.VIEW, "checked stats of asserts.");

            // Return a new Stats object with the calculated totals and fetched asset statistics
            return ResponseEntity.ok(AssetsStats.builder().totalMovableAsserts(totalMovableAsserts).totalAsserts(assertsJpaRepository.count()).totalFixedAsserts(totalFixedAsserts).totalCurrentAsserts(totalCurrentAsserts).assetsStats(assetsStats).build());
        } else {
            if (user.isStationAssigned()) {
                return ResponseEntity.ok(getStats(user.getId()));
            }
            return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to get stats for any station."));
        }
    }

    @Override
    public Page<AssertEntity> searchAsserts(AssetSearchCriteriaDTO criteria) {
        int page = criteria.getOffset() / criteria.getLimit();
        Pageable pageable = PageRequest.of(page, criteria.getLimit());
        return assertEntityRepository.searchAssets(
                criteria.getAssetDisc(),
                criteria.getAssetNumber(),
                criteria.getInvoiceNumber(),
                criteria.getLocation(),
                criteria.getOfficeLocation(),
                criteria.getDate(),
                criteria.getInitialRemarks(),
                criteria.getAssetType(),
                pageable
        );
    }

    @Override
    public long countStationsAssignedToUser(User currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            return stationRepository.getAllCount();
        } else{
            User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
            return user.getStations().size();
        }
    }

//    @Override
//    public List findAllAssertsOfStationsAssignedToUser(User currentUser) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
//            return assertEntityRepository.findAll();
//
//    }

//is this correct what needs to be corrected
@Override
public AssertsResponseDto getAllAssertsOfUserGroupedByStation(UserDTO currentUser, Boolean movable, Long stationId) {
    final List<AssertEntity> assertEntities = assertEntityRepository.findUserAsserts(currentUser.getId(), movable, stationId);
    return groupByAssertsByStation(assertEntities);
}


    @Override
    public List findAllMovableAssertsOfStationsAssignedToUser(User currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Check if the user has the ALL_STATION authority
            boolean hasAllStationRole = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(ROLE_AUTH.ALL_STATION));

            if (hasAllStationRole) {
                // Return all asserts if the user has the ALL_STATION role
                return assertEntityRepository.findAll();
            } else {
                // If not ALL_STATION, return an empty list
                return Collections.emptyList();
            }
        }

        // If authentication fails, return an empty list
        return Collections.emptyList();
    }


    public Page<AssertEntity> getAssertsOfStationsAssignedToUser(User currentUser, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return assertsJpaRepository.findAllAssertsOfStationsAssignedToUser(pageable, currentUser);
    }


    public ResponseEntity<?> getStats(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            // Fetch the total fixed asserts and total current asserts
            long totalFixedAsserts = assertsJpaRepository.countFixedAsserts();
            long totalCurrentAsserts = assertsJpaRepository.countCurrentAsserts();
            long totalMovableAsserts=assertsJpaRepository.countMovableAsserts();
            // Fetch asset statistics
            ArrayList<AssetItemStat> assetsStats = assertsJpaRepository.findAssertItemStatsByAssetDisc();
            userLogService.addLog(ActionType.VIEW, "checked stats of asserts.");

            // Return a new Stats object with the calculated totals and fetched asset statistics
            return ResponseEntity.ok(AssetsStats.builder().totalMovableAsserts(totalMovableAsserts).totalAsserts(assertsJpaRepository.count()).totalFixedAsserts(totalFixedAsserts).totalCurrentAsserts(totalCurrentAsserts).assetsStats(assetsStats).build());
        } else {
            if (user.isStationAssigned()) {
                return ResponseEntity.ok(getStats(user.getId()));
            }
            return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to get stats for any station."));
        }
    }

    public AssetsStats getStats(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            return getStatsForAllStations();
        } else {
            // Fetch the total fixed asserts and total current asserts
            long totalFixedAsserts = assertsJpaRepository.countFixedAssertsForUser(userId);
            long totalCurrentAsserts = assertsJpaRepository.countCurrentAssertsForUser(userId);
            long totalMovableAsserts=assertsJpaRepository.countMovableAsserts();

            // Fetch asset statistics
            List<AssetItemStat> assetsStats = assertsJpaRepository.findAssertItemStatsByAssetDiscForUser(userId);
//        userLogService.addLog(ActionType.VIEW, "checked stats of asserts.");

            // Return a new Stats object with the calculated totals and fetched asset statistics
            return AssetsStats.builder().totalMovableAsserts(totalMovableAsserts).totalAsserts(assertsJpaRepository.countAssertsForUserStations(userId)).totalFixedAsserts(totalFixedAsserts).totalCurrentAsserts(totalCurrentAsserts).assetsStats(assetsStats).build();
        }
    }

    public AssetsStats getStatsByStation(Long stationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            return getStatsForAllStations();
        } else {
            int totalAsserts = assertsJpaRepository.countAsserts(stationId);
            List<AssetItemStat> assetsStats = assertsJpaRepository.findAssertItemStatsByStation(stationId);
            return AssetsStats.builder().totalAsserts(totalAsserts).totalFixedAsserts(assertsJpaRepository.countFixedAsserts(stationId)).totalCurrentAsserts(assertsJpaRepository.countCurrentAsserts(stationId)).assetsStats(assetsStats).build();
        }
    }

    public AssetsStats getStatsForAllStations() {
        // Fetch the total fixed asserts and total current asserts
        long totalFixedAsserts = assertsJpaRepository.countFixedAsserts();
        long totalCurrentAsserts = assertsJpaRepository.countCurrentAsserts();
        long totalMovableAsserts=assertsJpaRepository.countMovableAsserts();
        // Fetch asset statistics
        List<AssetItemStat> assetsStats = assertsJpaRepository.findAssertItemStatsByAssetDisc();
        return AssetsStats.builder().totalMovableAsserts(totalMovableAsserts).totalAsserts(assertsJpaRepository.count()).totalFixedAsserts(totalFixedAsserts).totalCurrentAsserts(totalCurrentAsserts).assetsStats(assetsStats).build();
    }

//    public ResponseEntity<?> getAssertsForOwnStation(int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("lastModifiedDate").descending());
//        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
//        if (user.isStationAssigned()) {
//            return getAllAssertsByStation(user.getId(), pageRequest);
//        }
//        return ResponseEntity.ok(new ArrayList<AssertEntity>());
//    }

    public List<AssertEntity> getAsserts() {
        return assertsJpaRepository.findAll();
    }

    public ResponseEntity<?> getAllAssertsByStationMin(Long stationId) {
        return ResponseEntity.ok(assertRepository.getAllAssertsByStation(stationId));
    }

    private DownloadTokenService downloadTokenService;

    public ResponseEntity<?> getStatsToken() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            return downloadTokenService.createAssertDownloadToken();
        } else {
            if (user.isStationAssigned()) {
                return downloadTokenService.createStationAssertDownloadToken();
            }
            return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to get stats for any station."));
        }
    }

    private final DownloadTokenRepository downloadTokenRepository;

    public AssetsStats getStatsUsingToken(DownloadToken token) {
        return getStats(token.getCreator().getId());
    }

    public List<AssertEntity> getAssetPDFStation(Long stationId, String query) {
        return getAllAssertsByUserStation(stationId, query);
    }



    public List<AssertEntity> getAllAssetsByassetDisc() {
    List<AssertEntity>getAllAssetsByassetDisc=assertsJpaRepository.findAll();
    return getAllAssetsByassetDisc;

    }

Specification<AssertEntity>getSpecification(){
return (root, query, criteriaBuilder) -> {

 return   criteriaBuilder.equal(root.get("name"),"momo");


};
}

public List<AssertEntity>getAssertEntityByassetDisc(){


   Specification<AssertEntity>specification      = getSpecification();

  return  assertRepository.findAll(specification);

}

private Specification<AssertEntity>getSpecification(SpecificationInput specificationInput ){

      return (root, query, criteriaBuilder) -> {
      return  criteriaBuilder.equal(root.get(specificationInput.getColumnName()),
        specificationInput.getValue());   };}




public List<AssertEntity>getAssertEntityData(SpecificationInput specificationInput){
          Specification         <AssertEntity> specification=getSpecification(specificationInput);
 return    assertRepository.findAll(specification);

       }

    @Override
    public List<AssertEntity> findAllAssertsOfCurrentUser(UserDTO currentUser, Boolean movable, Long stationId) {
        // Fetch the user's asserts
        return assertEntityRepository.findUserAsserts(currentUser.getId(), movable, stationId);

    }

    @Override
    public AssertsResponseDto getAllAssertsGroupedByStation(Boolean movable, Long stationId) {
        final List<AssertEntity> assertEntities = assertEntityRepository.findByMoveable(movable, stationId);
        return groupByAssertsByStation(assertEntities);
    }

    private AssertsResponseDto groupByAssertsByStation(List<AssertEntity> assertEntities){
        // Group asserts by station ID, filtering out entities with null Station
        final Map<Long, List<AssertEntity>> groupedByStation = assertEntities.stream()
                .filter(entity -> entity.getStation() != null) // Exclude entities without a Station
                .collect(Collectors.groupingBy(entity -> entity.getStation().getStation_id()));

        List<StationAssertsDto> stationWiseAsserts =  groupedByStation.entrySet().stream()
                .map(entry -> {
                    final Long stationId = entry.getKey();
                    final List<AssertEntity> asserts = entry.getValue();
                    final String stationName = asserts.get(0).getStation().getStationName();
                    return new StationAssertsDto(stationId, stationName, asserts.size(), asserts);
                })
                .toList();
        return new AssertsResponseDto(assertEntities.size(), stationWiseAsserts);
    }

    @Override
    public List<AssetItemStat> getUserAssertStats(UserDTO currentUser) {
        List<AssetItemStat> stats = new ArrayList<>();
        stats.add(new AssetItemStat("total", assertEntityRepository.countUserAsserts(currentUser.getId(), null)));
        stats.add(new AssetItemStat("movable", assertEntityRepository.countUserAsserts(currentUser.getId(), true)));
        stats.add(new AssetItemStat("immovable", assertEntityRepository.countUserAsserts(currentUser.getId(), false)));
        return stats;
    }

}