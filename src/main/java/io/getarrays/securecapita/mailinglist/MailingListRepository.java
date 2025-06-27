package io.getarrays.securecapita.mailinglist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingListRepository      extends JpaRepository<MailingList, Long> {
}
