package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.domain.Role;
import io.getarrays.securecapita.roles.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    @Query("SELECT u FROM UserRole u WHERE u.user.id = :id")
    UserRole getRoleByUserId(Long id);
}
