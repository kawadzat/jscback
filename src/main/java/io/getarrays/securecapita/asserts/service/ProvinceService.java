package io.getarrays.securecapita.asserts.service;

import io.getarrays.securecapita.asserts.model.Province;
import io.getarrays.securecapita.asserts.repo.ProvinceEntityRepo;
import io.getarrays.securecapita.dto.UserDTO;

import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import io.getarrays.securecapita.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    private final ProvinceEntityRepo provinceEntityRepo;
    private final UserRepository1 userRepository1;

    public ResponseEntity<List<Province>> getAllProvinces() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = (UserDTO) authentication.getPrincipal();
        
        // Check if user has ALL_STATION permission
        boolean hasAllStationPermission = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()));
        
        if (hasAllStationPermission) {
            // Return all provinces for users with ALL_STATION permission
            return ResponseEntity.ok(provinceEntityRepo.findAll());
        }
        
        // For other users, return provinces associated with their stations
        List<Long> userStationIds = Arrays.stream(user.getStation().split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        
        List<Province> provinces = provinceEntityRepo.findAll().stream()
            .filter(province -> province.getStationId() != null && 
                    userStationIds.contains(province.getStationId()))
            .collect(Collectors.toList());
        
        // Return provinces if found, otherwise return empty list instead of forbidden
        return ResponseEntity.ok(provinces);
    }
}
