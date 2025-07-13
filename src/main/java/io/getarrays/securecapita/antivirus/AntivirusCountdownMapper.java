package io.getarrays.securecapita.antivirus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AntivirusCountdownMapper {

    private final AntivirusCountdownService countdownService;

    /**
     * Convert Antivirus entity to AntivirusCountdownDto with countdown information
     */
    public AntivirusCountdownDto toCountdownDto(Antivirus antivirus) {
        Map<String, Object> countdown = countdownService.getDetailedCountdown(antivirus);
        
        return AntivirusCountdownDto.builder()
            .antivirusId(antivirus.getId())
            .antivirusName(antivirus.getName())
            .antivirusKey(antivirus.getKey())
            .vendor(antivirus.getVendor())
            .version(antivirus.getVersion())
            .status(antivirus.getStatus())
            .isInstalled(antivirus.getIsInstalled())
            .hasExpirationDate((Boolean) countdown.get("hasExpirationDate"))
            .isExpired((Boolean) countdown.get("isExpired"))
            .urgencyLevel((String) countdown.get("urgencyLevel"))
            .formattedCountdown((String) countdown.get("formattedCountdown"))
            .message((String) countdown.get("message"))
            .days((Long) countdown.get("days"))
            .hours((Long) countdown.get("hours"))
            .minutes((Long) countdown.get("minutes"))
            .seconds((Long) countdown.get("seconds"))
            .totalHours((Long) countdown.get("totalHours"))
            .totalMinutes((Long) countdown.get("totalMinutes"))
            .totalSeconds((Long) countdown.get("totalSeconds"))
            .expiredDays((Long) countdown.get("expiredDays"))
            .expirationDate(antivirus.getLicenseExpirationDate())
            .currentDate((java.time.LocalDateTime) countdown.get("currentDate"))
            .laptopId(antivirus.getLaptop() != null ? antivirus.getLaptop().getId() : null)
            .laptopSerialNumber(antivirus.getLaptop() != null ? antivirus.getLaptop().getSerialNumber() : null)
            .laptopUser(antivirus.getLaptop() != null ? antivirus.getLaptop().getIssuedTo() : null)
            .build();
    }

    /**
     * Convert list of Antivirus entities to list of AntivirusCountdownDto
     */
    public List<AntivirusCountdownDto> toCountdownDtoList(List<Antivirus> antivirusList) {
        return antivirusList.stream()
            .map(this::toCountdownDto)
            .collect(Collectors.toList());
    }

    /**
     * Convert Antivirus entity to AntivirusCountdownDto with custom countdown data
     */
    public AntivirusCountdownDto toCountdownDto(Antivirus antivirus, Map<String, Object> customCountdown) {
        return AntivirusCountdownDto.builder()
            .antivirusId(antivirus.getId())
            .antivirusName(antivirus.getName())
            .antivirusKey(antivirus.getKey())
            .vendor(antivirus.getVendor())
            .version(antivirus.getVersion())
            .status(antivirus.getStatus())
            .isInstalled(antivirus.getIsInstalled())
            .hasExpirationDate((Boolean) customCountdown.get("hasExpirationDate"))
            .isExpired((Boolean) customCountdown.get("isExpired"))
            .urgencyLevel((String) customCountdown.get("urgencyLevel"))
            .formattedCountdown((String) customCountdown.get("formattedCountdown"))
            .message((String) customCountdown.get("message"))
            .days((Long) customCountdown.get("days"))
            .hours((Long) customCountdown.get("hours"))
            .minutes((Long) customCountdown.get("minutes"))
            .seconds((Long) customCountdown.get("seconds"))
            .totalHours((Long) customCountdown.get("totalHours"))
            .totalMinutes((Long) customCountdown.get("totalMinutes"))
            .totalSeconds((Long) customCountdown.get("totalSeconds"))
            .expiredDays((Long) customCountdown.get("expiredDays"))
            .expirationDate(antivirus.getLicenseExpirationDate())
            .currentDate((java.time.LocalDateTime) customCountdown.get("currentDate"))
            .laptopId(antivirus.getLaptop() != null ? antivirus.getLaptop().getId() : null)
            .laptopSerialNumber(antivirus.getLaptop() != null ? antivirus.getLaptop().getSerialNumber() : null)
            .laptopUser(antivirus.getLaptop() != null ? antivirus.getLaptop().getIssuedTo() : null)
            .build();
    }
} 