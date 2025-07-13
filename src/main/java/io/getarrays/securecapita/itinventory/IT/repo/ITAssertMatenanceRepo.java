package io.getarrays.securecapita.itinventory.IT.repo;

import io.getarrays.securecapita.itinventory.IT.Model.ITAssertMatenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITAssertMatenanceRepo extends JpaRepository<ITAssertMatenance, Long> {
    
    List<ITAssertMatenance> findByItAssertId(Long itAssertId);
    
    List<ITAssertMatenance> findByItAssertIdAndStatus(Long itAssertId, String status);
} 