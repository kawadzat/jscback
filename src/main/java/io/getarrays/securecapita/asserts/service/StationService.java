package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Station;
import io.getarrays.securecapita.asserts.model.StationsAssetsStatDto;
import io.getarrays.securecapita.asserts.model.StationsStatDto;
import io.getarrays.securecapita.asserts.repo.AssertEntityRepository;
import io.getarrays.securecapita.asserts.repo.StationRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.StationItemStat;
import io.getarrays.securecapita.dto.StationStats;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.exception.ResourceNotFoundException;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;
    private final UserRepository1 userRepository1;
    private final AssertEntityRepository assertRepository;
    private final AssertService assertService;

    /* to create user */
    public Station createStation(Station newStation) {
        Station createdStation = stationRepository.save(newStation);
        return createdStation;
    }

    /* updating the user */
    public Station updateStation(Station station) {

        Station updatedStation = stationRepository.save(station);

        return updatedStation;
    }


    public Station getStationById(Long stationId) {

        Optional<Station> optionalStation = stationRepository.findById(stationId);
        boolean isPresent = optionalStation.isPresent();

        if (isPresent) {

            Station station = optionalStation.get();
            return station;
        }

        return null;


    }


    public ResponseEntity<?> addAssert(Long stationId, Long assertId) {
        Optional<Station> optionStation = stationRepository.findById(stationId);
        if (optionStation.isEmpty()) {
            return ResponseEntity.badRequest().body("Station does not exist!");
        }
        Optional<AssertEntity> optionalAssert = assertRepository.findById(assertId);
        if (optionalAssert.isEmpty()) {
            return ResponseEntity.badRequest().body("Assert does not exist!");
        }
        System.out.println("Ok");
        optionalAssert.get().setStation(optionStation.get());
        assertRepository.save(optionalAssert.get());
        return ResponseEntity.ok("Added Assert to Station");
    }

    public ResponseEntity<?> getAllStations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            List<Station> stations = stationRepository.findAll();
            List<Map<String, Object>> transformedStations = stations.stream()
                    .map(station -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", station.getStation_id());
                        map.put("name", station.getStationName());
                        return map;
                    })
                    .toList();
            return ResponseEntity.ok(transformedStations);
        } else if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.VIEW_STATION.name()))) {
            User user = userRepository1.findById(((UserDTO) authentication.getPrincipal()).getId()).get();
            List<Station> stations =stationRepository.findAllByUserId(user.getId());
            List<Map<String, Object>> transformedStations = stations.stream()
                    .map(station -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", station.getStation_id());
                        map.put("name", station.getStationName());
                        return map;
                    })
                    .toList();
            return ResponseEntity.ok(transformedStations);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }

    public ResponseEntity<?> addUser(Long stationId, Long userId) {

        return ResponseEntity.ok(new CustomMessage("Added User to Station"));
    }

    public ResponseEntity<?> checkAssets(Long stationId) {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Optional<Station> stationOptional = stationRepository.findById(stationId);
        if (stationOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new CustomMessage("Station not found."));
        }
        if (!user.isStationAssigned(stationId)) {
            return ResponseEntity.status(401).body(new CustomMessage("You are not authorized in this station."));
        }
        stationRepository.editCheckAllAssetsForStation(stationOptional.get(), user);
        return ResponseEntity.ok(new CustomMessage("checked all assets till now."));
    }

    public ResponseEntity<?> getStats() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            ArrayList<StationItemStat> stationItemStats = stationRepository.findAssertItemStatsByAssetDisc();
            stationItemStats.forEach(stationItemStat -> {
                stationItemStat.setAssetsStats(assertService.getStatsByStation(stationItemStat.getStationId()));
            });
            return ResponseEntity.ok(StationStats.builder()
                    .totalStations(stationRepository.count())
                    .stationItemStats(stationItemStats)
                    .build());
        } else {
            if (user.isStationAssigned()) {
                List<StationItemStat> stationItemStats = stationRepository.findAssertItemStatsByUserStations(user.getId());
                stationItemStats.forEach(stationItemStat -> {
                    stationItemStat.setAssetsStats(assertService.getStatsByStation(stationItemStat.getStationId()));
                });
                return ResponseEntity.ok(StationStats.builder()
                        .totalStations(user.getStations().size())
                        .stationItemStats(stationItemStats)
                        .build());
            }
        }
        return ResponseEntity.badRequest().body(new CustomMessage("Not authorized for stats."));
    }

    public ResponseEntity<?> getStationStats() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            List<StationsStatDto> stationsStatDto = stationRepository.getStatforAllStations();
            List<StationsAssetsStatDto> stationsOfficesStatDtos = stationRepository.getAllAssetsStats();
            Map<String, List<StationsAssetsStatDto>> stationsToOfficesMap = new HashMap<>();
            for (StationsAssetsStatDto officeStatDto : stationsOfficesStatDtos) {
                stationsToOfficesMap.computeIfAbsent(officeStatDto.getStation(), k -> new ArrayList<>()).add(officeStatDto);
            }
            for (StationsStatDto stationStatDto : stationsStatDto) {
                if (stationStatDto.getAssets() > 0) {
                    stationStatDto.setAssetsList(stationsToOfficesMap.getOrDefault(stationStatDto.getName(), Collections.emptyList()));
                }
            }
            return ResponseEntity.ok(stationsStatDto);
        } else if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.VIEW_STATION.name()))) {
            if (!user.getStations().isEmpty()) {
                List<StationsStatDto> stationsStatDto = stationRepository.getStatForUserStations(user.getId());
                List<StationsAssetsStatDto> stationsOfficesStatDtos = stationRepository.getUserStationsAssetsStats(user.getId());
                System.out.println(stationsOfficesStatDtos);
                Map<String, List<StationsAssetsStatDto>> stationsToOfficesMap = new HashMap<>();
                for (StationsAssetsStatDto officeStatDto : stationsOfficesStatDtos) {
                    stationsToOfficesMap.computeIfAbsent(officeStatDto.getStation(), k -> new ArrayList<>()).add(officeStatDto);
                }
                for (StationsStatDto stationStatDto : stationsStatDto) {
                    if (stationStatDto.getAssets() > 0) {
                        stationStatDto.setAssetsList(stationsToOfficesMap.getOrDefault(stationStatDto.getName(), Collections.emptyList()));
                    }
                }
                return ResponseEntity.ok(stationsStatDto);
            }
        }
        return ResponseEntity.badRequest().body(new CustomMessage("Not authorized for stats."));
    }

    public ResponseEntity<List<Station>> getStationsByProvince(Long provinceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = (UserDTO) authentication.getPrincipal();
        
        // Check if user has ALL_STATION permission
        boolean hasAllStationPermission = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()));
        
        // Get all stations for the given province
        List<Station> stationsByProvince = stationRepository.findStationsByProvinceId(provinceId);
        
        if (hasAllStationPermission) {
            // Return all stations in the province for users with ALL_STATION permission
            return ResponseEntity.ok(stationsByProvince);
        }
        
        // For other users, return stations they are assigned to in this province
        List<Long> userStationIds = Arrays.stream(user.getStation().split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        
        List<Station> filteredStations = stationsByProvince.stream()
            .filter(station -> userStationIds.contains(station.getStation_id()))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(filteredStations);
    }

    public Station findStationByIdOrThrow(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station not found with id " + id));
    }
}
