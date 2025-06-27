package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.exception.ApiException;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static io.getarrays.securecapita.enumeration.VerificationType.PASSWORD;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Service
@RequiredArgsConstructor
public class UserService1 {
    private final UserRepository1 userRepository1;

    public User loadUserByUsername(String email) throws UsernameNotFoundException, NoSuchElementException {
        Optional<User> user = userRepository1.findByEmail(email);
        return user.orElseThrow();
//        return user.map(UserDTO::toDto).orElse(null);
    }

    public void resetPassword(String email) {
//        String expirationDate = format(addDays(new Date(), 1), DATE_FORMAT);
        Optional<User> user = userRepository1.findByEmail(email);
        if (user.isEmpty()) {
            throw new ApiException("An error occurred. Please try again.");
        }
        String verificationUrl = fromCurrentContextPath().path("/user/verify/" + PASSWORD.getType() + "/" + UUID.randomUUID().toString()).toUriString();
//        user.get().
//        jdbc.update(DELETE_PASSWORD_VERIFICATION_BY_USER_ID_QUERY, of("userId",  user.getId()));
//        jdbc.update(INSERT_PASSWORD_VERIFICATION_QUERY, of("userId",  user.getId(), "url", verificationUrl, "expirationDate", expirationDate));
//
//        sendEmail(user.getFirstName(), email, verificationUrl, PASSWORD);
//        log.info("Verification URL: {}", verificationUrl);
    }
}
