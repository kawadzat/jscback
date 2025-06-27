package io.getarrays.securecapita.asserts.master;

import io.getarrays.securecapita.asserts.repo.AssertsJpaRepository;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class MasterService {
    private final AssertsJpaRepository assertsJpaRepository;

    private final UserRepository1 userRepository1;

    public ResponseEntity<?> getRecentReport() {
//        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            Long previousYearBeginMS = getPreviousYearJan1stMS();
            List<MasterAssertsDTO> masterAssertsDTO = assertsJpaRepository.getMasterAssertsForRecent2years(previousYearBeginMS);
            return ResponseEntity.ok(masterAssertsDTO);
//        }
    }

    private Long getPreviousYearJan1stMS() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);        return calendar.getTimeInMillis();
    }
}
