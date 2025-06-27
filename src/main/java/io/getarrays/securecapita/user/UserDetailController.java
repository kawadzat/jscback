package io.getarrays.securecapita.user;

import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserDetailController {

    @Autowired
    private UserService userService;

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal UserDTO currentUser, @PathVariable(name = "userId") Long userId,
                                        @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.update(currentUser, userId, userUpdateDto));
    }

    @GetMapping("/departmentAndStationFellows")
    public ResponseEntity<?> getDepartmentAndStationFellows(@AuthenticationPrincipal UserDTO currentUser) {
        return ResponseEntity.ok(userService.getDepartmentAndStationFellows(currentUser));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUsers(@RequestParam(required = false) List<Long> stationIds, @RequestParam(required = false) List<Long> departmentIds) {
        return ResponseEntity.ok(userService.filterUsers(stationIds, departmentIds));
    }
}
