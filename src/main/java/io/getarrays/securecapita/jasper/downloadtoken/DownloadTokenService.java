package io.getarrays.securecapita.jasper.downloadtoken;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.domain.UserPrincipal;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DownloadTokenService {
    private final DownloadTokenRepository downloadTokenRepository;
    private final UserRepository1 userRepository1;
    private final UserRepository userRepository;

    public ResponseEntity<?> createDownloadToken() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        DownloadToken downloadToken = DownloadToken.builder()
                .tokenID(UUID.randomUUID())
                .creator(user)
                .isUsed(false)
                .expiryDate(new Timestamp(new Date().getTime() + 3600000))
                .build();
        return ResponseEntity.ok(new DownloadTokenDto(downloadTokenRepository.save(downloadToken)));
    }


    public Optional<DownloadToken> validateDownloadToken(UUID token) {
        Optional<DownloadToken> downloadTokenOptional = downloadTokenRepository.findById(token);
        if (downloadTokenOptional.isPresent() && !downloadTokenOptional.get().isUsed()) {
            downloadTokenOptional.get().setUsed(true);
            downloadTokenOptional.get().setUsedDate(new Timestamp(new Date().getTime()));
            downloadTokenRepository.save(downloadTokenOptional.get());
            downloadTokenOptional.get().setValid(true);
            return downloadTokenOptional;
        }
        return downloadTokenOptional;
    }

    private void authenticateUser(User user) {
        UserDetails userPrincipal = userRepository.loadUserByUsernamePrinciple(user.getEmail());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    public ResponseEntity<?> createAssertDownloadToken() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        DownloadToken downloadToken = DownloadToken.builder()
                .tokenID(UUID.randomUUID())
                .creator(user)
                .isUsed(false)
                .assertStat(true)
                .expiryDate(new Timestamp(new Date().getTime() + 3600000))
                .build();
        return ResponseEntity.ok(new DownloadTokenDto(downloadTokenRepository.save(downloadToken)));
    }

    public ResponseEntity<?> createStationAssertDownloadToken() {
        User user = userRepository1.findById(((UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()).get();
        DownloadToken downloadToken = DownloadToken.builder()
                .tokenID(UUID.randomUUID())
                .creator(user)
                .isUsed(false)
                .singleStation(true)
                .assertStat(true)
                .expiryDate(new Timestamp(new Date().getTime() + 3600000))
                .build();
        return ResponseEntity.ok(new DownloadTokenDto(downloadTokenRepository.save(downloadToken)));
    }
}
