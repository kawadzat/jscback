package io.getarrays.securecapita.asserts.repo;

import io.getarrays.securecapita.asserts.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceEntityRepo extends JpaRepository<Province, Long> {
}
