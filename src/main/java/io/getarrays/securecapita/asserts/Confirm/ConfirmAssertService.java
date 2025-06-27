package io.getarrays.securecapita.asserts.Confirm;

import io.getarrays.securecapita.asserts.checks.AssetCheck;
import io.getarrays.securecapita.asserts.checks.AssetChecksRepository;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.repo.AssertEntityRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmAssertService {

    private final UserRepository1 userRepository1;
    private final AssertEntityRepository assertRepository;
    private final ConfirmAssertRepo  confirmAssertRepo;


    public ResponseEntity<?> createConfirm(Long assetId) {
        Optional<AssertEntity> assertEntityOptional = assertRepository.findById(assetId);
        if (assertEntityOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomMessage("Asset not found."));
        }
        if(!canConfirm(assertEntityOptional.get().getStation())){
            return ResponseEntity.badRequest().body(new CustomMessage("Not authorized for assets confirm."));
        }
        Timestamp timestamp = new Timestamp(new Date().getTime());
     ConfirmAssert assetConfirm = ConfirmAssert.builder()
                .confirmedBy(userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get())
                .createdDate(timestamp)
                .updatedDate(timestamp)
                .asset(assertEntityOptional.get())
                .build();
     confirmAssertRepo.save(assetConfirm);
        return ResponseEntity.ok(new CustomMessage("Added new Asset Check to Station."));
    }

    private boolean canConfirm(Station station) {

        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> (r.getAuthority().contains(ROLE_AUTH.CONFIRM_ASSET.name()) || r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name())))) {
            return user.isStationAssigned(station.getStation_id());
        }
        return false;

    }






}
