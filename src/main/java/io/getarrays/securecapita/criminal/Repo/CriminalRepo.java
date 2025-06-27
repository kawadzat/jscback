package io.getarrays.securecapita.criminal.Repo;

import io.getarrays.securecapita.criminal.Domain.Criminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriminalRepo extends JpaRepository<Criminal, Long> {
    Criminal findByIdNumber(String idNumber);
    Page<Criminal> findByfullNameContaining(String fullname, Pageable pageable);
}
