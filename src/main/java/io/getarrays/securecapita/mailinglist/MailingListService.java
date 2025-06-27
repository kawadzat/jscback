package io.getarrays.securecapita.mailinglist;

import io.getarrays.securecapita.asserts.repo.StationRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailingListService {


    private MailingListRepository mailingListRepository;

    private final UserRepository1 userRepository1;
    private final StationRepository stationRepository;


    public MailingList save(MailingList mailingList) {
        return mailingListRepository.save(mailingList);
    }




}
