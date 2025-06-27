package io.getarrays.securecapita.officelocations;

import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.repo.StationRepository;
import io.getarrays.securecapita.exception.CustomMessage;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OfficeLocationService {
    private final OfficeLocationRepository officeLocationRepository;
    private final StationRepository stationRepository;

    public ResponseEntity<Object> getAll(Long stationId) {
        ArrayList<OfficeLocation> officeLocations = officeLocationRepository.findByStationId(stationId);
        return ResponseEntity.ok(officeLocations);
    }


    public ResponseEntity<Object> addLocation(Long stationId, OfficeLocation officeLocation) {
        Optional<OfficeLocation> optionalOfficeLocation = officeLocationRepository.findByStationAndName(stationId, officeLocation.getName());
        if (optionalOfficeLocation.isPresent()) {
            return ResponseEntity.badRequest().body(new CustomMessage("Office Location already exist."));
        }
        Optional<Station> stationOptional = stationRepository.findById(stationId);
        if (stationOptional.isPresent()) {
            officeLocation.setStation(stationOptional.get());
            officeLocationRepository.save(officeLocation);
            return ResponseEntity.ok(new CustomMessage("Location added successfully"));
        }
        return ResponseEntity.badRequest().body(new CustomMessage("Station Not found."));
    }
}
