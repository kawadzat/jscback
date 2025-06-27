package io.getarrays.securecapita.repository.implementation;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository1 extends JpaRepository<Role,Long> {
    @Query("SELECT r FROM Role r WHERE r.name = :role")
    Optional<Role> findByRoleName(String role);
}
