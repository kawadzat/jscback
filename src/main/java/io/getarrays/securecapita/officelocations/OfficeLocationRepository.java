package io.getarrays.securecapita.officelocations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface OfficeLocationRepository extends JpaRepository<OfficeLocation, Long> {

    @Query("Select o from OfficeLocation o where o.station.station_id=:stationId AND o.name=:location")
    Optional<OfficeLocation> findByStationAndName(Long stationId, String location);

    @Query("Select o from OfficeLocation o where o.station.station_id=:stationId")
    ArrayList<OfficeLocation> findByStationId(Long stationId);

}

