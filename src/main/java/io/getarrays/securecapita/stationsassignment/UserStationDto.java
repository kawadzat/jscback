package io.getarrays.securecapita.stationsassignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStationDto {
    private Long id;
    private String station;
    private String assignedBy;
    private Timestamp createdDate;


    public static List<UserStationDto> fromEntities(List<UserStation> userStationList) {
        List<UserStationDto> userStationDtos = new ArrayList<>();
        userStationList.forEach(userStation -> {
            userStationDtos.add(fromEntity(userStation));
        });
        return userStationDtos;
    }

    public static UserStationDto fromEntity(UserStation userStation) {
        return UserStationDto.builder()
                .assignedBy(ObjectUtils.isEmpty(userStation.getAssignedBy())?null:userStation.getAssignedBy().getFirstName())
                .id(userStation.getStation().getStation_id())
                .station(userStation.getStation().getStationName())
                .createdDate(userStation.getCreatedDate())
                .build();
    }
}
