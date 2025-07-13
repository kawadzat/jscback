package io.getarrays.securecapita.itinventory;

import io.getarrays.securecapita.antivirus.AntivirusService;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.getarrays.securecapita.antivirus.Antivirus;
import io.getarrays.securecapita.exception.NotAuthorizedException;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/laptop")
@RequiredArgsConstructor
public class LaptopController {


    @Autowired


  private  AntivirusService antivirusService;

    @Autowired
    private LaptopService laptopService;

    @PostMapping("/create")
    public ResponseEntity<CustomMessage> createLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                    @RequestBody @Valid LaptopDto laptopDto) throws Exception {
        return ResponseEntity.ok(new CustomMessage("Laptop Created Successfully", laptopService.createLaptop(currentUser,
                laptopDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomMessage> updateLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable("id") Long laptopId,
                                                    @RequestBody @Valid LaptopDto laptopDto) {
        try {
            // Validate that the laptop exists
            if (!laptopService.laptopExists(laptopId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage("Laptop not found with id: " + laptopId));
            }
            
            // Check if serial number is being changed and if it already exists
            LaptopDto existingLaptop = laptopService.getLaptopById(currentUser, laptopId);
            if (!existingLaptop.getSerialNumber().equals(laptopDto.getSerialNumber()) &&
                laptopService.serialNumberExists(laptopDto.getSerialNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new CustomMessage("Serial number already exists: " + laptopDto.getSerialNumber()));
            }
            
            // Update the laptop
            LaptopDto updatedLaptop = laptopService.updateLaptop(currentUser, laptopId, laptopDto);
            
            return ResponseEntity.ok(new CustomMessage(
                "Laptop Updated Successfully",
                updatedLaptop
            ));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CustomMessage("Laptop not found: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomMessage("Invalid data: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomMessage("Error updating laptop: " + e.getMessage()));
        }
    }

    /**
     * Handle invalid request body for laptop update
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<CustomMessage> handleInvalidRequestBody(org.springframework.http.converter.HttpMessageNotReadableException e) {
        String errorMessage = "Invalid request body. Please provide a valid JSON object with laptop details.";
        if (e.getMessage() != null && e.getMessage().contains("update")) {
            errorMessage = "Invalid request body. Expected JSON object but received string 'update'. Please provide laptop data in JSON format.";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new CustomMessage(errorMessage));
    }

    /**
     * Update laptop status only - accepts multiple formats
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<CustomMessage> changeLaptopStatus(@AuthenticationPrincipal UserDTO currentUser,
                                                          @PathVariable("id") Long laptopId,
                                                          @RequestBody Object statusRequest) {
        try {
            if (!laptopService.laptopExists(laptopId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage("Laptop not found with id: " + laptopId));
            }
            
            LaptopStatus newStatus;
            String reason = null;
            String notes = null;
            
            // Handle different input formats
            if (statusRequest instanceof String) {
                // Direct string value: "ISSUED"
                newStatus = LaptopStatus.valueOf(((String) statusRequest).toUpperCase());
            } else if (statusRequest instanceof java.util.Map) {
                // JSON object format
                java.util.Map<?, ?> statusMap = (java.util.Map<?, ?>) statusRequest;
                
                // Check if it's a simple object with status field
                Object statusValue = statusMap.get("status");
                if (statusValue != null) {
                    newStatus = LaptopStatus.valueOf(statusValue.toString().toUpperCase());
                    reason = (String) statusMap.get("reason");
                    notes = (String) statusMap.get("notes");
                } else {
                    // Try to parse as direct status value
                    String statusStr = statusRequest.toString();
                    if (statusStr.contains("=")) {
                        // Handle "status=ISSUED" format
                        String[] parts = statusStr.split("=");
                        if (parts.length >= 2) {
                            newStatus = LaptopStatus.valueOf(parts[1].toUpperCase());
                        } else {
                            throw new IllegalArgumentException("Invalid status format");
                        }
                    } else {
                        throw new IllegalArgumentException("Status field is required in request body");
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomMessage("Invalid status format. Expected string or JSON object with 'status' field"));
            }
            
            LaptopDto updatedLaptop = laptopService.changeLaptopStatus(currentUser, laptopId, newStatus);
            
            String message = "Laptop Status Updated Successfully";
            if (reason != null) {
                message += " - Reason: " + reason;
            }
            
            return ResponseEntity.ok(new CustomMessage(message, updatedLaptop));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomMessage("Invalid status value. Valid values are: WAITING_FOR_ACKNOWLEDGEMENT, AVAILABLE, ISSUED, PENDING_ACKNOWLEDGMENT, MAINTENANCE, RETIRED"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomMessage("Error updating laptop status: " + e.getMessage()));
        }
    }

    /**
     * Simple update endpoint that accepts a string action
     */
    @PutMapping("/{id}/action")
    public ResponseEntity<CustomMessage> performLaptopAction(@AuthenticationPrincipal UserDTO currentUser,
                                                           @PathVariable("id") Long laptopId,
                                                           @RequestBody String action) {
        try {
            if (!laptopService.laptopExists(laptopId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage("Laptop not found with id: " + laptopId));
            }
            
            LaptopDto updatedLaptop = null;
            String message = "";
            
            switch (action.toLowerCase().trim()) {
                case "update":
                case "refresh":
                    // Get current laptop data
                    updatedLaptop = laptopService.getLaptopById(currentUser, laptopId);
                    message = "Laptop data refreshed successfully";
                    break;
                case "available":
                    updatedLaptop = laptopService.changeLaptopStatus(currentUser, laptopId, LaptopStatus.AVAILABLE);
                    message = "Laptop status changed to AVAILABLE";
                    break;
                case "issued":
                    updatedLaptop = laptopService.changeLaptopStatus(currentUser, laptopId, LaptopStatus.ISSUED);
                    message = "Laptop status changed to ISSUED";
                    break;
                case "maintenance":
                    updatedLaptop = laptopService.changeLaptopStatus(currentUser, laptopId, LaptopStatus.MAINTENANCE);
                    message = "Laptop status changed to MAINTENANCE";
                    break;
                case "retired":
                    updatedLaptop = laptopService.changeLaptopStatus(currentUser, laptopId, LaptopStatus.RETIRED);
                    message = "Laptop status changed to RETIRED";
                    break;
                case "waiting":
                case "PENDING_ACKNOWLEDGMENT":
                    updatedLaptop = laptopService.changeLaptopStatus(currentUser, laptopId, LaptopStatus.PENDING_ACKNOWLEDGMENT);
                    message = "Laptop status changed to PENDING_ACKNOWLEDGMENT";
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CustomMessage("Invalid action: " + action + ". Valid actions are: update, refresh, available, issued, maintenance, retired, waiting"));
            }
            
            return ResponseEntity.ok(new CustomMessage(message, updatedLaptop));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CustomMessage("Error performing action: " + e.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LaptopDto>> getAllLaptops(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(laptopService.getAllLaptops(currentUser));
    }

    @GetMapping("/issued")
    public ResponseEntity<List<LaptopDto>> getIssuedLaptops(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(laptopService.getIssuedLaptops(currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaptopDto> getLaptopById(@AuthenticationPrincipal UserDTO currentUser,
                                                  @PathVariable("id") Long laptopId) {
        return ResponseEntity.ok(laptopService.getLaptopById(currentUser, laptopId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomMessage> deleteLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                    @PathVariable("id") Long laptopId) {
        laptopService.deleteLaptop(currentUser, laptopId);
        return ResponseEntity.ok(new CustomMessage("Laptop Deleted Successfully"));
    }


    @GetMapping("/serial-number-exists/{serialNumber}")
    public ResponseEntity<Boolean> serialNumberExists(@PathVariable("serialNumber") String serialNumber) {
        return ResponseEntity.ok(laptopService.serialNumberExists(serialNumber));
    }

    @PostMapping("/antivirus/laptop/{id}")
    public ResponseEntity<Antivirus> addAntivirusToLaptop(
            @PathVariable("id") Long laptopId,
            @RequestBody Antivirus antivirus) {
        Antivirus created = antivirusService.addAntivirusToLaptop(laptopId, antivirus);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/all-maintenance")
    public ResponseEntity<List<LaptopDto>> getAllLaptopWithMaintenance(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(laptopService.getAllLaptopsWithMaintenanceStatus(currentUser));
    }





    @GetMapping("/all-issued")
    public ResponseEntity<List<LaptopDto>> getAllLaptopWithISSUED(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(laptopService.getIssuedLaptops(currentUser));
    }




    @GetMapping("/{id}/with-retired")
    public ResponseEntity<LaptopDto> getLaptopWithRetiredStatus(@AuthenticationPrincipal UserDTO currentUser, @PathVariable("id") Long laptopId) {
        return ResponseEntity.ok(laptopService.getLaptopWithRetiredStatusById(currentUser, laptopId));
    }





    @GetMapping("/all-retired")
    public ResponseEntity<List<LaptopDto>> getAllLaptopWithRETIRED(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(laptopService.getAllLaptopsWithRetiredStatus(currentUser));
    }

    // Issue laptop with pending acknowledgment
    @PutMapping("/{id}/issue-with-acknowledgment")
    public ResponseEntity<CustomMessage> issueLaptopWithAcknowledgment(@AuthenticationPrincipal UserDTO currentUser,
                                                                      @PathVariable("id") Long laptopId,
                                                                      @RequestBody @Valid LaptopDto laptopDto) {
        return ResponseEntity.ok(new CustomMessage(
                "Laptop issued and pending station admin acknowledgment",
                laptopService.issueLaptopWithAcknowledgment(currentUser, laptopId, laptopDto)
        ));
    }

    // Station admin acknowledges laptop issuance
    @PostMapping("/{id}/acknowledge-issuance")
    public ResponseEntity<CustomMessage> acknowledgeLaptopIssuance(@AuthenticationPrincipal UserDTO currentUser,
                                                                  @PathVariable("id") Long laptopId,
                                                                  @RequestBody @Valid LaptopAcknowledgmentDto acknowledgmentDto) {
        return ResponseEntity.ok(new CustomMessage(
                "Laptop issuance acknowledged by station admin",
                laptopService.acknowledgeLaptopIssuance(currentUser, laptopId, acknowledgmentDto)
        ));
    }

    // Get laptops pending acknowledgment for a station
    @GetMapping("/pending-acknowledgment/{station}")
    public ResponseEntity<List<LaptopDto>> getLaptopsPendingAcknowledgment(@AuthenticationPrincipal UserDTO currentUser,
                                                                          @PathVariable("station") String station) {
        return ResponseEntity.ok(laptopService.getLaptopsPendingAcknowledgment(currentUser, station));
    }

    // Get acknowledgment history for a laptop
    @GetMapping("/{id}/acknowledgment")
    public ResponseEntity<LaptopAcknowledgmentDto> getLaptopAcknowledgment(@PathVariable("id") Long laptopId) {
        return ResponseEntity.ok(laptopService.getLaptopAcknowledgment(laptopId));
    }



    // Step 2: Acknowledge a laptop
    @PostMapping("/acknowledge")
    public LaptopAcknowledgmentDto acknowledgeLaptop(
            @RequestParam Long laptopId,
            @RequestBody UserDTO acknowledger,
            @RequestParam(required = false) String notes
    ) {
        return laptopService.acknowledgeLaptop(acknowledger, laptopId, notes);
    }




    @PostMapping("/issue")
    public ResponseEntity<CustomMessage> issueLaptop(
            @AuthenticationPrincipal UserDTO currentUser,
            @RequestBody @Valid LaptopDto laptopDto) {
        return ResponseEntity.ok(new CustomMessage(
                "Laptop issued and pending acknowledgment",
                laptopService.issueLaptop(currentUser, laptopDto)
        ));
    }



    @GetMapping("/all-acknowledged")
    public ResponseEntity<List<LaptopDto>> getAllAcknowledged() {
        return ResponseEntity.ok(laptopService.getAllAcknowledged());
    }














    // Manually acknowledge a laptop that is in PENDING_ACKNOWLEDGMENT status
    @PostMapping("/{id}/manual-acknowledge")
    public ResponseEntity<CustomMessage> manuallyAcknowledgeLaptop(@AuthenticationPrincipal UserDTO currentUser,
                                                                  @PathVariable("id") Long laptopId,
                                                                  @RequestBody(required = false) String acknowledgmentNotes) {
        try {
            LaptopDto updatedLaptop = laptopService.manuallyAcknowledgeLaptop(currentUser, laptopId, acknowledgmentNotes);
            return ResponseEntity.ok(new CustomMessage(
                    "Laptop manually acknowledged and status changed to ISSUED",
                    updatedLaptop
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CustomMessage("Invalid request: " + e.getMessage()));
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new CustomMessage("Not authorized: " + e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomMessage("Not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomMessage("Error acknowledging laptop: " + e.getMessage()));
        }









    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<LaptopDto>> getLaptopsByStatus(@PathVariable("status") LaptopStatus status) {
        return ResponseEntity.ok(laptopService.getLaptopsByStatus(status));
    }
}

