package io.getarrays.securecapita.meeting;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.repository.UserRepository;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import io.getarrays.securecapita.service.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MeetingService {

    private final EmailService emailService;
    private final MeetingRepository meetingRepository;
    private final UserRepository<User> userRepository;
    private final UserRepository1 userRepository1; // Assuming this is used elsewhere

    public Object createMeeting(UserDTO currentUser, @Valid MeetingDto meetingDto) {
        // 1. Create Meeting from DTO
        Meeting meeting = new Meeting();

        meeting.setTime(meetingDto.getTime());
        meeting.setAgenda(meetingDto.getAgenda());
        meeting.setPlace(meetingDto.getPlace());
        meeting.setAttendeesEmail(meetingDto.getAttendeesEmail());

        // 2. Set createdBy (assumes you have User -> Meeting relationship)
        Optional<User> creator = Optional.ofNullable(userRepository.get(currentUser.getId()));
        if (creator.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        meeting.setCreatedBy(creator.get());

        // 3. Save the meeting
        Meeting savedMeeting = meetingRepository.save(meeting);

        // 4. Send email notification
        sendMeetingCreatedEmail(savedMeeting);

        // 5. Return saved object (could be mapped to a DTO if preferred)
        return savedMeeting;
    }

    // Private helper method for sending meeting invitation emails
    private void sendMeetingCreatedEmail(Meeting meeting) {
        meeting.getAttendeesEmail().forEach(email -> {
            String subject = "New Meeting Scheduled";
            String content =
                    "Dear Attendee,\n\n" +
                            "You have been invited to a meeting.\n\n" +

                            "Date: " + meeting.getMeetingDate() + "\n" +
                            "Time: " + meeting.getTime() + "\n" +
                            "Place: " + meeting.getPlace() + "\n" +
                            "Organized by: " + meeting.getAgenda() + "\n\n" +
                            "Please mark your calendar.\n\n" +
                            "Best regards,\nMeeting Scheduler";

            emailService.sendEmail(email, subject, content);
        });
    }
}



