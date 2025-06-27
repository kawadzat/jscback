package io.getarrays.securecapita.meeting;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
public class MeetingDto {  @NotNull(message = "Meeting date is required")
private LocalDate meetingDate;

    @NotNull(message = "Time is required")
    private LocalTime time;

    @NotBlank(message = "Agent name is required")
    private String agenda;

    @NotBlank(message = "Place is required")
    private String place;

    @NotEmpty(message = "Attendees email list must not be empty")
    private List<@Email(message = "Invalid email format") String> attendeesEmail;
}
