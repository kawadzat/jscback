package io.getarrays.securecapita.jasper.downloadtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
public class DownloadTokenController {
    private final DownloadTokenService downloadTokenService;

    @GetMapping("/generate")
    public ResponseEntity<?> generateToken() {
        return downloadTokenService.createDownloadToken();
    }
}
