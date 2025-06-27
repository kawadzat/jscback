package io.getarrays.securecapita.asserts.repo;

import io.getarrays.securecapita.asserts.master.MasterAssertsDTO;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.AssetItemStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssertsJpaRepository extends JpaRepository<AssertEntity, Long> {
//find want to get all asserts of stations assigned to logged in user
//this end point
    @Query("SELECT ae FROM AssertEntity ae JOIN ae.station st JOIN UserStation us ON us.station = st   WHERE :currentUser = us.user ORDER BY ae.id")
    Page<AssertEntity> findAllAssertsOfStationsAssignedToUser(Pageable pageable, @Param("currentUser") User currentUser);
//
//    @Query("SELECT ae FROM AssertEntity ae WHERE ae.user = :currentUser ORDER BY ae.id")
//    Page<AssertEntity> findAllMovableAssertsOfStationsAssignedToUser(Pageable pageable, @Param("currentUser") User currentUser);
//
//    @Query("SELECT ae FROM AssertEntity ae WHERE ae.user = :currentUser ORDER BY ae.id")
//    Page<AssertEntity> findAllIMovableAssertsOfStationsAssignedToUser(Pageable pageable, @Param("currentUser") User currentUser);

    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.assertType) = 'fixed'")
    long countFixedAsserts();

    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.assertType) = 'fixed' AND a.station.station_id=:stationId")
    long countFixedAsserts(Long stationId);

    @Query("SELECT COUNT(DISTINCT a) FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE LOWER(a.assertType) = 'fixed' AND us.user.id = :userId")
    long countFixedAssertsForUser(@Param("userId") Long userId);


    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.assertType) = 'current'")
    long countCurrentAsserts();

    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE movable = true")
    long countMovableAsserts();

//bad asserts
    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.initialRemarks) = 'bad'")
    long countBadAsserts();






    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.assertType) = 'current'")
    int countCurrentAssertsPerStation();

    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.assertType) = 'current' AND a.station.station_id=:stationId")
    int countCurrentAsserts(Long stationId);

    @Query("SELECT COUNT(DISTINCT a) FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE LOWER(a.assertType) = 'current' AND us.user.id = :userId")
    int countCurrentAssertsForUser(@Param("userId") Long userId);

    @Query("SELECT new io.getarrays.securecapita.dto.AssetItemStat(" +
            "a.assetDisc, " +
            "COUNT(a.id)) FROM AssertEntity a " +
            "GROUP BY a.assetDisc")
    ArrayList<AssetItemStat> findAssertItemStatsByAssetDisc();

//    @Query("SELECT new io.getarrays.securecapita.dto.AssetItemStat(" +
//            "a.assetDisc, " +
//            "COUNT(a.id)) FROM AssertEntity a " +
//            "WHERE a.station.station_id=:stationId " +
//            "GROUP BY a.assetDisc")
//    ArrayList<AssetItemStat> findAssertItemStatsByAssetDisc(Long stationId);

    @Query("SELECT new io.getarrays.securecapita.dto.AssetItemStat(" +
            "a.assetDisc, " +
            "COUNT(DISTINCT a.id)) " +
            "FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user.id = :userId " +
            "GROUP BY a.assetDisc")
    List<AssetItemStat> findAssertItemStatsByAssetDiscForUser(@Param("userId") Long userId);

    @Query("SELECT new io.getarrays.securecapita.dto.AssetItemStat(" +
            "a.assetDisc, " +
            "COUNT(DISTINCT a.id)) " +
            "FROM AssertEntity a " +
            "WHERE a.station.station_id = :stationId " +
            "GROUP BY a.assetDisc")
    List<AssetItemStat> findAssertItemStatsByStation(@Param("stationId") Long stationId);
    @Query("SELECT a FROM AssertEntity a WHERE a.assetDisc = :assetDisc")
    Optional<AssertEntity> findByName(@Param("assetDisc") String assetDisc);

    @Query("SELECT a FROM AssertEntity a WHERE a.assetDisc = :assetDisc and a.station.station_id=:selectedStationID")
    Optional<AssertEntity> findByNameAndStation(String assetDisc, Long selectedStationID);

    @Query("SELECT a FROM AssertEntity a WHERE a.serialNumber = :serialNumber and a.station.station_id=:selectedStationID")
    Optional<AssertEntity> findBySerialAndStation(String serialNumber, Long selectedStationID);

    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE a.station.station_id=:stationId")
    int countAsserts(Long stationId);






    @Query("SELECT a FROM AssertEntity a WHERE a.assetNumber = :assetNumber and a.station.station_id=:selectedStationID")
    Optional<AssertEntity> findByAssetAndStation(String assetNumber, Long selectedStationID);

    @Query("SELECT COUNT(DISTINCT a) FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user.id = :userId")
    int countAssertsForUserStations(@Param("userId") Long userId);


    @Query("SELECT new io.getarrays.securecapita.asserts.master.MasterAssertsDTO(" +
            "a.assetDisc, " +
            "COUNT(a.id), " +
            "SUM(CASE WHEN a.date < :previousYearMilli THEN 1 ELSE 0 END)," +
            "(SELECT a2.initialRemarks FROM AssertEntity a2 WHERE a2.assetDisc = a.assetDisc ORDER BY a2.date DESC LIMIT 1)" +
            ", 100) " +
            "FROM AssertEntity a " +
            "GROUP BY a.assetDisc")
    List<MasterAssertsDTO> getMasterAssertsForRecent2years(@Param("previousYearMilli") Long previousYearMilli);

//    @EntityGraph(value = "assert-entity-graph",attributePaths = {"station"})
//    for: approvedBy,initiatedBy and assertEntity
     @EntityGraph(value = "assert-entity-graph",attributePaths = {"approvedBy","initiatedBy","assertEntity.inspections"})
    @Query("SELECT a FROM AssertEntity a WHERE a.id = :assertId")
    Optional<AssertEntity> findByAssertId(Long assertId);
}
