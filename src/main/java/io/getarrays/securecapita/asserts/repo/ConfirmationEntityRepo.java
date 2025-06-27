package io.getarrays.securecapita.asserts.repo;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.ConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationEntityRepo  extends JpaRepository<ConfirmationEntity, Long> {
}
