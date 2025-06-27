package io.getarrays.securecapita.mailinglist;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingListRepo  extends JpaRepository<MailingList, Long> {
}
