package io.getarrays.securecapita.asserts.LongTermAsserts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FixedAssertRepository extends JpaRepository<FixedAssert, Long> {




    @Query("SELECT a FROM FixedAssert a WHERE a.fixedAssetNumber = :assetNumber and a.station.station_id=:selectedStationID")
    Optional<FixedAssert> findByFixedAssetAndStation(String assetNumber, Long selectedStationID);




}
