package io.getarrays.securecapita.userlogs;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin/logs")
@RequiredArgsConstructor
public class UserLogController {
    private final UserLogService userLogService;
    @GetMapping("/list")
    public ResponseEntity<Object> listLogs(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return userLogService.getUserLogs(page,size);
    }
}
