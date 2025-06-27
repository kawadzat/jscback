package io.getarrays.securecapita.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.enumeration.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 3/21/2023
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
@Entity
@Table(name = "user_events")
public class UserEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private EventType type;
    private String description;
    private String device;
    private String ipAddress;
    private LocalDateTime createdAt;

    private Long userId;
}
