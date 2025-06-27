package io.getarrays.securecapita.repository.implementation;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.exception.ApiException;
import io.getarrays.securecapita.repository.RoleRepository;
import io.getarrays.securecapita.repository.UserRoleRepository;
import io.getarrays.securecapita.roles.UserRole;
import io.getarrays.securecapita.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.Optional;


import static io.getarrays.securecapita.enumeration.RoleType.*;
import static io.getarrays.securecapita.query.RoleQuery.*;
import static java.util.Map.of;
import static java.util.Objects.requireNonNull;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 8/28/2022
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {
    private final NamedParameterJdbcTemplate jdbc;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository1 roleRepository1;
    private UserRepository1 userRepository1;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list() {


        log.info("Fetching all roles");
        try {
            return jdbc.query(SELECT_ROLES_QUERY, new RoleRowMapper());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again.");
        }


    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id: {}", roleName, userId);
//        try {
//            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name", roleName), new RoleRowMapper());
//            jdbc.update(INSERT_ROLE_TO_USER_QUERY, of("userId", userId, "roleId", requireNonNull(role).getId()));
//        } catch (EmptyResultDataAccessException exception) {
//            throw new ApiException("No role found by name: " + ROLE_USER.name());
//
//        } catch (Exception exception) {
//            log.error(exception.getMessage());
//            throw new ApiException("An error occurred. Please try again.");
//        }
        Optional<Role> roleOptional = roleRepository1.findByRoleName(roleName);
        if (roleOptional.isEmpty()) {
            throw new ApiException("No role found by name: " + roleName);
        }
        Optional<User> optionalUser = userRepository1.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ApiException("No user found by id: " + userId);
        }
        optionalUser.get().removeAllRole();
        optionalUser.get().getRoles().stream().findAny().ifPresent((userRole -> {
            userRole.setRole(roleOptional.get());
            userRoleRepository.save(userRole);
        }));
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        log.info("Fetching role for user id: {}", userId);
        UserRole userRole = userRoleRepository.getRoleByUserId(userId);
        if (userRole != null) {
            return userRole.getRole();
        } else {
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
//        log.info("Updating role for user id: {}", userId);
//        try {
//            Role role = jdbc.queryForObject(SELECT_ROLE_BY_NAME_QUERY, of("name", roleName), new RoleRowMapper());
//            jdbc.update(UPDATE_USER_ROLE_QUERY, of("roleId", role.getId(), "userId", userId));
//        } catch (EmptyResultDataAccessException exception) {
//            throw new ApiException("No role found by name: " + roleName);
//        } catch (Exception exception) {
//            log.error(exception.getMessage());
//            throw new ApiException("An error occurred. Please try again.");
//        }
        addRoleToUser(userId, roleName);
    }
}
