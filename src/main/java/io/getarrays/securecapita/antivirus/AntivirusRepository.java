package io.getarrays.securecapita.antivirus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AntivirusRepository extends JpaRepository<Antivirus, Long> {

} 