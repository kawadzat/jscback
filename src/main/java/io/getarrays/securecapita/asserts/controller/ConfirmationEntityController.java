package io.getarrays.securecapita.asserts.controller;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.ConfirmationEntity;
import io.getarrays.securecapita.asserts.service.AssertService;
import io.getarrays.securecapita.asserts.service.ConfirmationEntityService;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/confirmation")
@RequiredArgsConstructor
public class ConfirmationEntityController {
    private final ConfirmationEntityService    confirmationEntityService ;


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Validated ConfirmationEntity newConfirmationEntity) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.CREATE_ASSET.name()))) {
            return  confirmationEntityService.createConfirmationEntity(newConfirmationEntity);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));

    }


}
