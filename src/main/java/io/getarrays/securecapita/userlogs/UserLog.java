package io.getarrays.securecapita.userlogs;

import io.getarrays.securecapita.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_logs")
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
