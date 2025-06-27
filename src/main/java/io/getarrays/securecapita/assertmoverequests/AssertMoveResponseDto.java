package io.getarrays.securecapita.assertmoverequests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssertMoveResponseDto {
    private Long id;
    private Timestamp date;
    private String initiatedBy;
    private String approvedBy;
    private String name;
    private String reason;
    private String requestedLocation;
    private String currentLocation;
    private String requestedStation;
    private String currentStation;
    private AssertMoveStatus status;
    private String serial;


    public static List<AssertMoveResponseDto> toList(List<AssertMoveRequest> moveRequestList) {
        List<AssertMoveResponseDto> assertMoveResponseDtoList = new ArrayList<>();
        moveRequestList.forEach((assertMoveRequest -> assertMoveResponseDtoList.add(
                AssertMoveResponseDto.builder()
                        .id(assertMoveRequest.getId())
                        .name(assertMoveRequest.getAssertEntity().getAssetDisc())
                        .serial(assertMoveRequest.getAssertEntity().getSerialNumber())
                        .requestedLocation(assertMoveRequest.getOfficeLocation().getName())
                        .reason(assertMoveRequest.getReason())
                        .requestedStation(assertMoveRequest.getOfficeLocation().getStation().getStationName())
                        .currentLocation(assertMoveRequest.getAssertEntity().getOfficeLocation().getName())
                        .currentStation(assertMoveRequest.getAssertEntity().getStation().getStationName())
                        .status(assertMoveRequest.getStatus())
                        .date(assertMoveRequest.getCreatedDate())
                        .initiatedBy(assertMoveRequest.getInitiatedBy()!=null ? assertMoveRequest.getInitiatedBy().getEmail()+" ("+assertMoveRequest.getInitiatedBy().getFirstName()+")" : null)
                        .approvedBy(assertMoveRequest.getApprovedBy() != null ? assertMoveRequest.getApprovedBy().getEmail()+" ("+assertMoveRequest.getApprovedBy().getFirstName()+")" : null)
                        .build()
        )));
        return assertMoveResponseDtoList;
    }
}
