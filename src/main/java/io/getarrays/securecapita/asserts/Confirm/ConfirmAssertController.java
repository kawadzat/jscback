package io.getarrays.securecapita.asserts.Confirm;

import io.getarrays.securecapita.asserts.checks.AssetsChecksService;
import io.getarrays.securecapita.repository.implementation.UserRepository1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/assert/confirm")
@RequiredArgsConstructor
public class ConfirmAssertController {

    private final UserRepository1 userRepository1;
    private final ConfirmAssertService  confirmAssertService;
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam("assetId") Long assetId) {
        return  confirmAssertService.createConfirm(assetId)  ;                                 //assetsChecksService.createCheck(assetId);
    }



}
