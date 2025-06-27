package io.getarrays.securecapita.domain;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.roles.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static io.getarrays.securecapita.dtomapper.UserDTOMapper.fromUser;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 12/25/2022
 */

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {
    private final User user;
    private final Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return stream(this.role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
//i need to implement this
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
//stock is stores department
    //stock item is the department asking for stocks
    //that can be removed, was trying to figure out how to make it,jpa vs jdbc
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserDTO getUser() {
        return fromUser(this.user, UserRole.builder().role(role).build());
    }
}
