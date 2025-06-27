package io.getarrays.securecapita.userlogs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserLogsRepository extends JpaRepository<UserLog, UUID> {

    @Query("SELECT new io.getarrays.securecapita.userlogs.UserLogDto(u.uuid, u.timestamp, u.actionType, u.description, u.user.email) FROM UserLog u")
    Page<UserLogDto> findAllUserLogs(Pageable pageable);
}
