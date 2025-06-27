package io.getarrays.securecapita.asserts.LongTermAsserts;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.repo.InspectionRepository;
import io.getarrays.securecapita.asserts.repo.StationRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.userlogs.ActionType;
import io.getarrays.securecapita.userlogs.UserLogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FixedAssertsService {
 private final UserRepository1 userRepository1;
private final FixedAssertRepository fixedAssertRepository;
    private final InspectionRepository inspectionRepository;
    private final StationRepository stationRepository;

    private final UserLogService userLogService;
    /* to create user */
    public ResponseEntity<?> createFixedAssert(FixedAssertDto fixedassertDto) throws Exception {
        // Check for duplicate entry
        Optional<FixedAssert> duplicatedFixedAssert = fixedAssertRepository.findByFixedAssetAndStation(
                fixedassertDto.getFixedAssetNumber(), fixedassertDto.getSelectedStationID());

        if (duplicatedFixedAssert.isPresent()) {
            return ResponseEntity.status(422)
                    .body(new CustomMessage("Found Duplicate Entry. Please check again."));
        }

        // Validate station existence
        Optional<Station> optionalStation = stationRepository.findById(fixedassertDto.getSelectedStationID());
        if (optionalStation.isEmpty()) {
            throw new Exception("Station not found");
        }

        // Create new FixedAssert object
        FixedAssert newFixedAssert = new FixedAssert();
        newFixedAssert.setStation(optionalStation.get());
      //  newFixedAssert.setAcquisitionDate(fixedassertDto.getAcquisitionDate());
        newFixedAssert.setFixedAssetNumber(fixedassertDto.getFixedAssetNumber());

        // Get authenticated user
        User user = userRepository1.findById(
                ((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
        ).orElseThrow(() -> new Exception("User not found"));

        newFixedAssert.setPreparedBy(user);

        // Save the entity
        FixedAssert createdFixedAssert = fixedAssertRepository.save(newFixedAssert);

        // Log the creation
        userLogService.addLog(ActionType.CREATED,
                "Created FixedAssert successfully. FixedAssert: " + newFixedAssert.getAssetDisc());

        return ResponseEntity.ok(createdFixedAssert);
    }


    public Object getFixedAsserts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("lastModifiedDate").descending());

        return fixedAssertRepository.findAll(pageRequest);
    }
}
