package io.getarrays.securecapita.asserts.controller;

import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.service.StationService;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "/station")
@RequiredArgsConstructor
public class StationController {

    /* to create user */

    private final StationService stationService;
    private final UserRepository1 userRepository1;

    @PostMapping("/create")
    public Station createStation(@RequestBody Station newStation) {
        Station createdStation = stationService.createStation(newStation);
        return createdStation;
    }

    //is this the project i cant see the enum
    @PutMapping("/update/{id}")
    public Station updateStation(@PathVariable("id") Long stationId, @RequestBody Station station) {

        Station oldstation = stationService.getStationById(stationId);


        oldstation.setStationName(station.getStationName());


        Station updatedStation = stationService.updateStation(oldstation);

        return updatedStation;
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStations() {
        return stationService.getAllStations();
    }


    //add assert to station
    @PostMapping("/addAssertToStation")
    public ResponseEntity<?> addAssertToStation(@RequestParam("assertId") Long assertId, @RequestParam("stationId") Long stationId) {
        return stationService.addAssert(stationId, assertId);
    }

    @PostMapping("/addUserToStation")
    public ResponseEntity<?> addUserToStation(@RequestParam("userId") Long userId, @RequestParam("stationId") Long stationId) {
        return stationService.addUser(stationId, userId);
    }


    //check
    @PostMapping("/check")
    public ResponseEntity<?> checkAssets(@RequestParam("stationId") Long stationId) {
        return stationService.checkAssets(stationId);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getOverallStats() {
            return stationService.getStats();

    }

    @GetMapping("/stationsStat")
    public ResponseEntity<?> getStationStats() {
        return stationService.getStationStats();
    }

    @GetMapping("/by-province/{provinceId}")
    public ResponseEntity<List<Station>> getStationsByProvince(@PathVariable Long provinceId) {
        return stationService.getStationsByProvince(provinceId);
    }
}