package io.getarrays.securecapita.asserts.checks;

import io.getarrays.securecapita.asserts.service.AssertService;
import io.getarrays.securecapita.asserts.service.StationService;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/assert/checks")
@RequiredArgsConstructor
public class AssertsChecksController {
    private final UserRepository1 userRepository1;
    private final AssetsChecksService assetsChecksService;


    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam("stationId") Long stationId,@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return assetsChecksService.getChecksMaster(stationId,page,size);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam("assetId") Long assetId) {
        return assetsChecksService.createCheck(assetId);
    }
}
