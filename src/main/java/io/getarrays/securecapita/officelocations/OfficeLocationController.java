package io.getarrays.securecapita.officelocations;

import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/station/location")
@AllArgsConstructor
public class OfficeLocationController {
    private final OfficeLocationService officeLocationService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllLocations(@RequestParam("stationId") Long stationId) {
        return officeLocationService.getAll(stationId);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addLocation(@RequestParam("stationId") Long stationId, @RequestBody @Validated OfficeLocation officeLocation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.CREATE_ASSET.name()))) {
            return officeLocationService.addLocation(stationId, officeLocation);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }





}
