package io.getarrays.securecapita.itinventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LaptopAcknowledgmentRepository extends JpaRepository<LaptopAcknowledgment, Long> {
    
    Optional<LaptopAcknowledgment> findByLaptopId(Long laptopId);
    
    boolean existsByLaptopId(Long laptopId);
    
    @Query("SELECT la FROM LaptopAcknowledgment la WHERE la.laptop.station = :stationName")
    List<LaptopAcknowledgment> findByStationName(@Param("stationName") String stationName);
    
    @Query("SELECT la FROM LaptopAcknowledgment la WHERE la.acknowledgedBy.id = :userId")
    List<LaptopAcknowledgment> findByAcknowledgedByUserId(@Param("userId") Long userId);
}
