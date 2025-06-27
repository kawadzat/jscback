package io.getarrays.securecapita.asserts.repo;

import io.getarrays.securecapita.asserts.model.ITServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITServicesRepository extends JpaRepository<ITServices , Long> {
}
