package io.getarrays.securecapita.meeting;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/meeting")
@RequiredArgsConstructor
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @PostMapping("/meetings")
    public ResponseEntity<CustomMessage> createMeeting(@AuthenticationPrincipal UserDTO currentUser,
                                                       @RequestBody @Valid MeetingDto meetingDto) throws Exception {
        return ResponseEntity.ok(new CustomMessage("Meeting Created Successfully",
                meetingService.createMeeting(currentUser, meetingDto)));
    }



}
