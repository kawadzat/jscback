package io.getarrays.securecapita.userlogs;

import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLogDto {
    private UUID uuid;
    private Timestamp timestamp;
    private ActionType actionType;
    private String description;
    private String email;
}
