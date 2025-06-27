package io.getarrays.securecapita.stationsassignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserStationRepo extends JpaRepository<UserStation, Long> {

    @Query("Select u from UserStation u where u.user.id=:userId")
    List<UserStation> findAllByUser(Long userId);

    @Query("Select count(u) from UserStation u where u.user.id=:userId")
    Long countByUser(Long userId);

    @Query("Select u from UserStation u where u.user.id=:userId and u.station.station_id=:stationId")
    Optional<UserStation> findAllByUserAndStation(Long userId, Long stationId);
}
