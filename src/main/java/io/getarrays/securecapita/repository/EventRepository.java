package io.getarrays.securecapita.repository;

import io.getarrays.securecapita.domain.UserEvent;
import io.getarrays.securecapita.enumeration.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 3/21/2023
 */
@Repository
public interface EventRepository extends JpaRepository<UserEvent, Long> {

    @Query("Select e from UserEvent e where userId = :userId ")
    List<UserEvent> getEventsByUserId(Long userId);
}

