package io.getarrays.securecapita.meeting;

import io.getarrays.securecapita.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
