package io.getarrays.securecapita.itinventory.IT.Service;

import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.repo.StationRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.itinventory.IT.Model.ITAssert;
import io.getarrays.securecapita.itinventory.IT.Model.ITAssertMatenance;
import io.getarrays.securecapita.itinventory.IT.dto.ITAssertDto;
import io.getarrays.securecapita.itinventory.IT.repo.ITAssertMatenanceRepo;
import io.getarrays.securecapita.itinventory.IT.repo.ITAssertRepo;
import io.getarrays.securecapita.officelocations.OfficeLocation;
import io.getarrays.securecapita.officelocations.OfficeLocationRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class ITAssertService implements ITAssertServiceInterface {

    private final ITAssertRepo itAssertRepo;
    private final ITAssertMatenanceRepo itAssertMatenanceRepo;
    private final UserRepository1 userRepository;
    private final OfficeLocationRepository officeLocationRepository;
    private final StationRepository stationRepository;

    @Override
    public ITAssert createITAssert(ITAssertDto itAssertDto) {
        ITAssert itAssert = new ITAssert();
        
        // Set basic fields
        itAssert.setDate(itAssertDto.getDate());
        itAssert.setAssetDisc(itAssertDto.getAssetDisc());
        itAssert.setAssetNumber(itAssertDto.getAssetNumber());
        itAssert.setQuantity(itAssertDto.getQuantity());
        itAssert.setMovable(itAssertDto.getMovable());
        itAssert.setSerialNumber(itAssertDto.getSerialNumber());
        itAssert.setInvoiceNumber(itAssertDto.getInvoiceNumber());
        itAssert.setAssertType(itAssertDto.getAssertType());
        itAssert.setLocation(itAssertDto.getLocation());
        itAssert.setInitialRemarks(itAssertDto.getInitialRemarks());
        
        // Set office location if provided
        if (itAssertDto.getOfficeLocationId() != null) {
            OfficeLocation officeLocation = officeLocationRepository.findById(itAssertDto.getOfficeLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Office location not found with id: " + itAssertDto.getOfficeLocationId()));
            itAssert.setOfficeLocation(officeLocation);
        }
        
        // Set station if provided
        if (itAssertDto.getStationId() != null) {
            Station station = stationRepository.findById(itAssertDto.getStationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + itAssertDto.getStationId()));
            itAssert.setStation(station);
        }
        
        // Set prepared by user if provided
        if (itAssertDto.getPreparedById() != null) {
            User preparedBy = userRepository.findById(itAssertDto.getPreparedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + itAssertDto.getPreparedById()));
            itAssert.setPreparedBy(preparedBy);
        }
        
        // Set checked by user if provided
        if (itAssertDto.getCheckedById() != null) {
            User checkedBy = userRepository.findById(itAssertDto.getCheckedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + itAssertDto.getCheckedById()));
            itAssert.setCheckedBy(checkedBy);
        }
        
        // Set assigned users if provided
        if (itAssertDto.getAssignedUserIds() != null && !itAssertDto.getAssignedUserIds().isEmpty()) {
            Set<User> assignedUsers = new HashSet<>();
            for (Long userId : itAssertDto.getAssignedUserIds()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                assignedUsers.add(user);
            }
            itAssert.setAssignedUsers(assignedUsers);
        }
        
        return itAssertRepo.save(itAssert);
    }
    
    @Override
    public ITAssert getITAssertById(Long id) {
        return itAssertRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IT Asset not found with id: " + id));
    }
    
    @Override
    public List<ITAssert> getAllITAsserts() {
        return itAssertRepo.findAll();
    }
    
    @Override
    public ITAssert updateITAssert(Long id, ITAssertDto itAssertDto) {
        ITAssert existingITAssert = getITAssertById(id);
        
        // Update basic fields
        existingITAssert.setDate(itAssertDto.getDate());
        existingITAssert.setAssetDisc(itAssertDto.getAssetDisc());
        existingITAssert.setAssetNumber(itAssertDto.getAssetNumber());
        existingITAssert.setQuantity(itAssertDto.getQuantity());
        existingITAssert.setMovable(itAssertDto.getMovable());
        existingITAssert.setSerialNumber(itAssertDto.getSerialNumber());
        existingITAssert.setInvoiceNumber(itAssertDto.getInvoiceNumber());
        existingITAssert.setAssertType(itAssertDto.getAssertType());
        existingITAssert.setLocation(itAssertDto.getLocation());
        existingITAssert.setInitialRemarks(itAssertDto.getInitialRemarks());
        
        // Update office location if provided
        if (itAssertDto.getOfficeLocationId() != null) {
            OfficeLocation officeLocation = officeLocationRepository.findById(itAssertDto.getOfficeLocationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Office location not found with id: " + itAssertDto.getOfficeLocationId()));
            existingITAssert.setOfficeLocation(officeLocation);
        }
        
        // Update station if provided
        if (itAssertDto.getStationId() != null) {
            Station station = stationRepository.findById(itAssertDto.getStationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Station not found with id: " + itAssertDto.getStationId()));
            existingITAssert.setStation(station);
        }
        
        // Update prepared by user if provided
        if (itAssertDto.getPreparedById() != null) {
            User preparedBy = userRepository.findById(itAssertDto.getPreparedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + itAssertDto.getPreparedById()));
            existingITAssert.setPreparedBy(preparedBy);
        }
        
        // Update checked by user if provided
        if (itAssertDto.getCheckedById() != null) {
            User checkedBy = userRepository.findById(itAssertDto.getCheckedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + itAssertDto.getCheckedById()));
            existingITAssert.setCheckedBy(checkedBy);
        }
        
        // Update assigned users if provided
        if (itAssertDto.getAssignedUserIds() != null) {
            Set<User> assignedUsers = new HashSet<>();
            for (Long userId : itAssertDto.getAssignedUserIds()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                assignedUsers.add(user);
            }
            existingITAssert.setAssignedUsers(assignedUsers);
        }
        
        return itAssertRepo.save(existingITAssert);
    }
    
    @Override
    public void deleteITAssert(Long id) {
        if (!itAssertRepo.existsById(id)) {
            throw new ResourceNotFoundException("IT Asset not found with id: " + id);
        }
        itAssertRepo.deleteById(id);
    }

    @Override
    public void addITAssertMatenanceToITAssert(Long id, ITAssertMatenance itAssertMatenance) {
        // Find the IT asset by ID
        ITAssert itAssert = itAssertRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IT Asset not found with id: " + id));

        // Set the IT asset relationship
        itAssertMatenance.setItAssert(itAssert);

        // Save the maintenance record
        itAssertMatenanceRepo.save(itAssertMatenance);
    }


}