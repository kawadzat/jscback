package io.getarrays.securecapita.assertmoverequests;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.repo.AssertsJpaRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
//import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import io.getarrays.securecapita.officelocations.OfficeLocationRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AssertMoveService {
    private final MoveLocationRepository moveLocationRepository;
    private final AssertsJpaRepository assertEntityRepository;
    private final OfficeLocationRepository officeLocationRepository;
    private final UserRepository1 userRepository1;

    public ResponseEntity<Object> getAll(int page, int size, long stationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository1.findById(((UserDTO) authentication.getPrincipal()).getId()).get();
        Page<AssertMoveRequest> officeLocations;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            if (stationId == 0) {
                officeLocations = moveLocationRepository.findAll(pageRequest);
            } else {
                officeLocations = moveLocationRepository.findAllWithStationId(stationId, pageRequest);
            }
        } else {
            if (stationId == 0) {
                officeLocations = moveLocationRepository.findByUserIdAndAssignedStations(user.getId(), pageRequest);
            } else {
                officeLocations = moveLocationRepository.findByUserIdAndAssignedStationsId(stationId, user.getId(), pageRequest);
            }
        }

        return ResponseEntity.ok(AssertMoveResponseDto.toList(officeLocations.get().toList()));
    }

    @Transactional
    public ResponseEntity<Object> approve(Long assertRequestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.APPROVE_MOVE_ASSET.name()))) {
            Optional<AssertMoveRequest> assertMoveRequest = moveLocationRepository.findMoveRequest(assertRequestId);
            User user = userRepository1.findById(((UserDTO) authentication.getPrincipal()).getId()).get();
            if (assertMoveRequest.isPresent() && assertMoveRequest.get().getStatus() == AssertMoveStatus.PENDING) {
                Optional<AssertEntity> optionalAssert = assertEntityRepository.findById(assertMoveRequest.get().getAssertEntity().getId());
                //initialize assert

                if (optionalAssert.isPresent()) {
                    optionalAssert.get().setOfficeLocation(assertMoveRequest.get().getOfficeLocation());
                    optionalAssert.get().setLocation(assertMoveRequest.get().getOfficeLocation().getName());
                    optionalAssert.get().setStation(assertMoveRequest.get().getOfficeLocation().getStation());
                    assertMoveRequest.get().setStatus(AssertMoveStatus.APPROVED);
                    assertMoveRequest.get().setApprovedBy(user);
                    assertMoveRequest.get().setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                    assertEntityRepository.save(optionalAssert.get());
                    moveLocationRepository.save(assertMoveRequest.get());
                    return ResponseEntity.ok(new CustomMessage("Assert moved to " + assertMoveRequest.get().getOfficeLocation().getName()));
                }
            }
            return ResponseEntity.ok(new CustomMessage("Request not found."));
        }
        return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to perform this action."));
    }


    public ResponseEntity<Object> reject(Long assertRequestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.APPROVE_MOVE_ASSET.name()))) {
            Optional<AssertMoveRequest> assertMoveRequest = moveLocationRepository.findMoveRequest(assertRequestId);
            if (assertMoveRequest.isPresent()) {
                assertMoveRequest.get().setStatus(AssertMoveStatus.REJECTED);
                assertMoveRequest.get().setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                moveLocationRepository.save(assertMoveRequest.get());
                return ResponseEntity.ok(new CustomMessage("Assert move request rejected."));
            }
            return ResponseEntity.ok(new CustomMessage("Request not found."));
        }
        return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to perform this action."));
    }

    public ResponseEntity<Object> addRequest(AssertMoveRequestDto assertMoveService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.REQUEST_MOVE_ASSET.name()))) {
            User user = userRepository1.findById(((UserDTO) authentication.getPrincipal()).getId()).get();

            Optional<AssertEntity> optionalAssert = assertEntityRepository.findByAssertId(assertMoveService.getAssertId());
            if (moveLocationRepository.findByAssertEntityIdAndStatus(assertMoveService.getAssertId(), AssertMoveStatus.PENDING).isPresent()) {
                return ResponseEntity.badRequest().body(new CustomMessage("Assert already in pending list."));
            }
            if (optionalAssert.isPresent()) {
                Optional<OfficeLocation> optionalOfficeLocation = officeLocationRepository.findById(assertMoveService.getLocationId());
                if (optionalOfficeLocation.isPresent()) {
                    AssertMoveRequest assertMoveRequest = AssertMoveRequest.builder()
                            .assertEntity(optionalAssert.get())
                            .officeLocation(optionalOfficeLocation.get())
                            .initiatedBy(user)
                            .reason(assertMoveService.getReason())
                            .status(AssertMoveStatus.PENDING)
                            .build();
                    assertMoveRequest.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    assertMoveRequest.setUpdatedDate(assertMoveRequest.getCreatedDate());
                    moveLocationRepository.save(assertMoveRequest);
                    return ResponseEntity.ok(new CustomMessage("Assert move requested to " + optionalOfficeLocation.get().getName()));
                }
                return ResponseEntity.badRequest().body(new CustomMessage("Location not found."));
            }
            return ResponseEntity.badRequest().body(new CustomMessage("Assert not found."));
        }
        return ResponseEntity.badRequest().body(new CustomMessage("You are not authorized to perform this action."));

    }
}
