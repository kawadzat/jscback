package io.getarrays.securecapita.itinventory.IT.repo;

import io.getarrays.securecapita.itinventory.IT.Model.ITAssert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ITAssertRepo   extends JpaRepository<ITAssert, Long> {


    @Query("SELECT COUNT(a) FROM ITAssert a WHERE a.station.station_id=:stationId")
    int countAsserts(Long stationId);

}
