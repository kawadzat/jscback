package io.getarrays.securecapita.asserts.repo;

import io.getarrays.securecapita.asserts.dto.StationAssertsDto;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.AssertResponseDto;
import io.getarrays.securecapita.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssertEntityRepository extends PagingAndSortingRepository<AssertEntity, Long>, JpaRepository<AssertEntity, Long>, ListCrudRepository<AssertEntity, Long>, JpaSpecificationExecutor<AssertEntity> {
//here

    @Query("SELECT COUNT(DISTINCT ae.station) FROM AssertEntity ae JOIN ae.station st JOIN UserStation us ON us.station = st   WHERE :currentUser = us.user")
    long countStationsAssignedToUser(@Param("currentUser") User currentUser);


    @Query("SELECT a FROM AssertEntity a WHERE a.movable = true")
    List<AssertEntity> findAllMovableAssets();

    @Query("SELECT COUNT(a) FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user = :user")
    long countAsserts(@Param("user") User user);



    @Query("SELECT a FROM AssertEntity a WHERE a.movable = true")
    List<AssertEntity> findAllIMovableAssets();

    @Query("SELECT COUNT(a) FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user = :user")
    long ccountAsserts(@Param("user") User user);




    @Query("SELECT COUNT(a) FROM AssertEntity a WHERE LOWER(a.initialRemarks) = 'bad'")
    int countBadAsserts();

    //    @Query("select  from Assert where a.stationName=:n" )
//    List<AssertEntity> getAllAssertsByStation(@Param("n")String stationName);
    @Query("SELECT a FROM AssertEntity a WHERE a.station.station_id = :stationId " +
            " AND (LOWER(a.assetDisc) LIKE %:query% " +
            " OR LOWER(a.assetNumber) LIKE %:query% " +
            " OR LOWER(a.serialNumber) LIKE %:query% " +
            " OR LOWER(a.invoiceNumber) LIKE %:query% " +
            " OR LOWER(a.assertType) LIKE %:query% " +
            " OR LOWER(a.location) LIKE %:query% " +
            " OR ( :query IS NULL OR LOWER(REPLACE(TRIM(a.officeLocation.name), ' ', '')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), ' ', ''))) " +
            " OR LOWER(a.initialRemarks) LIKE %:query% )")
    Page<AssertEntity> getAllAssertsByStationPage(@Param("stationId") Long stationId,
                                                  @Param("query") String query,
                                                  Pageable pageRequest);

    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user.id = :userId AND s.station_id = :stationId " +
            " AND (LOWER(a.assetDisc) LIKE %:query% " +
            " OR LOWER(a.assetNumber) LIKE %:query% " +
            " OR LOWER(a.serialNumber) LIKE %:query% " +
            " OR LOWER(a.invoiceNumber) LIKE %:query% " +
            " OR LOWER(a.assertType) LIKE %:query% " +
            " OR LOWER(a.location) LIKE %:query% " +
            " OR (:query IS NULL OR LOWER(REPLACE(TRIM(a.officeLocation.name), ' ', '')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), ' ', ''))) " +
            " OR LOWER(a.initialRemarks) LIKE %:query% )")
    Page<AssertEntity> getAssertsByUserStationPaged(
            @Param("userId") Long userId,
            @Param("stationId") Long stationId,
            @Param("query") String query,
            Pageable pageable
    );

    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user.id = :userId AND s.station_id = :stationId " +
            " AND (:query IS NULL OR :query = '' OR (LOWER(a.assetDisc) LIKE %:query% " +
            " OR LOWER(a.assetNumber) LIKE %:query% " +
            " OR LOWER(a.serialNumber) LIKE %:query% " +
            " OR LOWER(a.invoiceNumber) LIKE %:query% " +
            " OR LOWER(a.assertType) LIKE %:query% " +
            " OR LOWER(a.location) LIKE %:query% " +
            " OR (:query IS NULL OR LOWER(REPLACE(TRIM(a.officeLocation.name), ' ', '')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), ' ', ''))) " +
            " OR LOWER(a.initialRemarks) LIKE %:query% ))")
    List<AssertEntity> getAssertsByUserStation(
            @Param("userId") Long userId,
            @Param("stationId") Long stationId,
            @Param("query") String query
    );

    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE s.station_id = :stationId " +
            " AND (:query IS NULL OR :query = '' OR (LOWER(a.assetDisc) LIKE %:query% " +
            " OR LOWER(a.assetNumber) LIKE %:query% " +
            " OR LOWER(a.serialNumber) LIKE %:query% " +
            " OR LOWER(a.invoiceNumber) LIKE %:query% " +
            " OR LOWER(a.assertType) LIKE %:query% " +
            " OR LOWER(a.location) LIKE %:query% " +
            " OR (:query IS NULL OR LOWER(REPLACE(TRIM(a.officeLocation.name), ' ', '')) LIKE LOWER(REPLACE(CONCAT('%', :query, '%'), ' ', ''))) " +
            " OR LOWER(a.initialRemarks) LIKE %:query% ))")
    List<AssertEntity> getAssertsByStation(
            @Param("stationId") Long stationId,
            @Param("query") String query
    );

    //without query
    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user.id = :userId AND s.station_id = :stationId ")
    List<AssertEntity> getAssertsByUserStation(
            @Param("userId") Long userId,
            @Param("stationId") Long stationId
    );

    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE s.station_id = :stationId ")
    List<AssertEntity> getAssertsByStation(
            @Param("stationId") Long stationId
    );

//    @Query("SELECT DISTINCT a FROM AssertEntity a " +
//            "JOIN a.station s " +
//            "JOIN UserStation us ON us.station = s " +
//            "WHERE 1 = 1 AND (:assetNumber IS NULL or a.assetNumber = %:assetNumber%)")
//    Optional<List<AssertEntity>> getAssetEntityBySearch(
//            @Param("assetNumber") String assetNumber,
//
//    );


    @Query("SELECT new io.getarrays.securecapita.asserts.model.AssertResponseDto(a.id, a.assetDisc,a.assetNumber) FROM AssertEntity a where a.station.station_id=:stationId")
    List<AssertResponseDto> getAllAssertsByStation(Long stationId);

    @EntityGraph(value = "assert-entity-graph")
    @Query("SELECT a from AssertEntity a where a.id=?1")
    Optional<AssertEntity> findByAssetId(Long id);


    @Query("SELECT a FROM AssertEntity a " +
            "WHERE (:assetDisc IS NULL OR a.assetDisc LIKE CONCAT('%', :assetDisc, '%')) " +
            "AND (:assetNumber IS NULL OR a.assetNumber LIKE CONCAT('%', :assetNumber, '%')) " +
            "AND (:invoiceNumber IS NULL OR a.invoiceNumber LIKE CONCAT('%', :invoiceNumber, '%')) " +
            "AND (:location IS NULL OR a.location LIKE CONCAT('%', :location, '%')) " +
            "AND (:officeLocation IS NULL OR LOWER(REPLACE(TRIM(a.officeLocation.name), ' ', '')) LIKE LOWER(REPLACE(CONCAT('%', :officeLocation, '%'), ' ', ''))) " +
            "AND (:dateOn IS NULL OR date(a.date) = date(:dateOn)) "+
            "AND (:initialRemarks IS NULL OR a.initialRemarks = :initialRemarks) " +
            "AND (:assetType IS NULL OR a.assertType = :assetType)")
    Page<AssertEntity> searchAssets(
            @Param("assetDisc") String assetDisc,
            @Param("assetNumber") String assetNumber,
            @Param("invoiceNumber") String invoiceNumber,
            @Param("location") String location,
            @Param("officeLocation") String officeLocation,
            @Param("dateOn") Date date,
            @Param("initialRemarks") String initialRemarks,
            @Param("assetType") String assetType,
            Pageable pageable
    );

    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE " +
            "(:stationId IS NULL OR s.station_id=:stationId)" +
            "AND us.user.id = :userId " +
            "AND (:movable IS NULL OR " +
            "(a.movable = :movable OR (:movable = false AND a.movable IS NULL)))")
    List<AssertEntity> findUserAsserts(@Param("userId") Long userId, @Param("movable") Boolean movable, @Param("stationId")  Long stationId);

    @Query("SELECT DISTINCT a FROM AssertEntity a " +
            "JOIN a.station s " +
            "WHERE " +
            "(:stationId IS NULL OR s.station_id=:stationId)" +
            "AND(:movable IS NULL OR " +
            "(a.movable = :movable OR (:movable = false AND a.movable IS NULL)))")
    List<AssertEntity> findByMoveable(@Param("movable") Boolean movable, @Param("stationId")  Long stationId);

    @Query("SELECT COUNT(DISTINCT a) FROM AssertEntity a " +
            "JOIN a.station s " +
            "JOIN UserStation us ON us.station = s " +
            "WHERE us.user.id = :userId " +
            "AND (:movable IS NULL OR " +
            "(a.movable = :movable OR (:movable = false AND a.movable IS NULL)))")
    Long countUserAsserts(@Param("userId") Long userId, @Param("movable") Boolean movable);


}
