package io.getarrays.securecapita.utils;

import io.getarrays.securecapita.domain.UserPrincipal;
import io.getarrays.securecapita.dto.UserDTO;
import org.springframework.security.core.Authentication;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 3/13/2023
 */
public class UserUtils {
    public static UserDTO getAuthenticatedUser(Authentication authentication) {
        return ((UserDTO) authentication.getPrincipal());
    }

    public static UserDTO getLoggedInUser(Authentication authentication) {
        return ((UserPrincipal) authentication.getPrincipal()).getUser();
    }
}
