package io.getarrays.securecapita.criminal.Controller;

import io.getarrays.securecapita.criminal.service.CriminalService;
import io.getarrays.securecapita.domain.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@RestController

@RequiredArgsConstructor

@RequestMapping("/criminal")
public class CriminalControl {

    private final CriminalService criminalService;

    @GetMapping("/criminals")
    public ResponseEntity<HttpResponse>getUsers(@RequestParam Optional<String> fullname,
                                                @RequestParam Optional<Integer> page,
                                                @RequestParam Optional<Integer> size) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        //throw new RuntimeException("Forced exception for testing");
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(of("page", criminalService.getCriminalByIdNumber(fullname.orElse("")), page.orElse(0), size.orElse(10)))
                        .message("Users Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());

    }


    // Upload Image by idNumber
    @PostMapping("/{idNumber}/uploadImage")
    public ResponseEntity<String> uploadCriminalImage(@PathVariable String idNumber,
                                                      @RequestParam("file") MultipartFile file) {
        try {
            criminalService.uploadCriminalImage(idNumber, file);
            return ResponseEntity.ok("Image uploaded successfully for ID: " + idNumber);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading image: " + e.getMessage());
        }
    }

    // Retrieve Image by idNumber
    @GetMapping("/{idNumber}/image")
    public ResponseEntity<byte[]> getCriminalImage(@PathVariable String idNumber) {
        byte[] imageData = criminalService.getCriminalImageByIdNumber(idNumber);
        if (imageData != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}