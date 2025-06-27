package io.getarrays.securecapita.asserts.repo;

import io.getarrays.securecapita.asserts.model.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InspectionRepository   extends JpaRepository<Inspection, Long> {

    class ServicesRepository {
    }
}
