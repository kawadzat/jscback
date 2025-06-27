package io.getarrays.securecapita.asserts.Confirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmAssertRepo extends JpaRepository<ConfirmAssert, Long> {
}
