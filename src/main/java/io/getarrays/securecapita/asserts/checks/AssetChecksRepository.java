package io.getarrays.securecapita.asserts.checks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface AssetChecksRepository extends JpaRepository<AssetCheck, Long> {




    @Query("SELECT new io.getarrays.securecapita.asserts.checks.AssertChecksResponseDto(a.id, a.asset.assetDisc,a.asset.serialNumber, a.checkedBy.firstName, a.checkedBy.email, a.updatedDate) FROM AssetCheck a")
    Page<AssertChecksResponseDto> findAllChecks(PageRequest pageRequest);

    @Query("SELECT new io.getarrays.securecapita.asserts.checks.AssertChecksResponseDto(a.id, a.asset.assetDisc,a.asset.serialNumber, a.checkedBy.firstName, a.checkedBy.email, a.updatedDate) FROM AssetCheck a WHERE a.asset.station.station_id IN :stationIds")
    Page<AssertChecksResponseDto> findAllChecks(List<Long> stationIds, PageRequest pageRequest);

    @Query("SELECT new io.getarrays.securecapita.asserts.checks.AssertChecksResponseDto(a.id, a.asset.assetDisc,a.asset.serialNumber, a.checkedBy.firstName, a.checkedBy.email, a.updatedDate) FROM AssetCheck a WHERE a.asset.station.station_id =:stationId")
    Page<AssertChecksResponseDto> findAllChecks(Long stationId, PageRequest pageRequest);

}
