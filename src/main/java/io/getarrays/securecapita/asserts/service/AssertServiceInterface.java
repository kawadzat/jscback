package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.dto.AssertsResponseDto;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.AssetItemStat;
import io.getarrays.securecapita.dto.AssetSearchCriteriaDTO;
import io.getarrays.securecapita.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AssertServiceInterface {


    void addInspectionToAssertEntity(Long id, Inspection inspection);

    ResponseEntity<?> getAllAssertsByStation(Long userId,Long stationId, String query,PageRequest pageRequest);

    public Inspection getInspection(Long id);


    AssertEntity getAssertEntityById(Long assertEntityId);

    ResponseEntity<?> getAllAssertsByUserStation(Long userId, Long stationId, String query,PageRequest pageRequest);

    ResponseEntity<?> getAllAssertsByUserStation(Long userId, String query,PageRequest pageRequest);

    ResponseEntity<?> getStats();

    Page<AssertEntity> searchAsserts(AssetSearchCriteriaDTO criteria);

    long countStationsAssignedToUser(User currentUser);

    List  findAllMovableAssertsOfStationsAssignedToUser(User currentUser);
    AssertsResponseDto  getAllAssertsOfUserGroupedByStation(UserDTO currentUser, Boolean moveable, Long stationId);
    List  findAllAssertsOfCurrentUser(UserDTO currentUser, Boolean moveable, Long stationId);
    AssertsResponseDto getAllAssertsGroupedByStation(Boolean moveable, Long stationId);
    List<AssetItemStat> getUserAssertStats(UserDTO currentUser);
}
