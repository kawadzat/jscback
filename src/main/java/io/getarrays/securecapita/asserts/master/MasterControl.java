package io.getarrays.securecapita.asserts.master;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/masterAssert/report")
@RequiredArgsConstructor
public class MasterControl {
    private final MasterService masterService;

    @GetMapping("")
    public ResponseEntity<?> getReport() {
        return masterService.getRecentReport();
    }
}
