package io.getarrays.securecapita.mailinglist;

import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
import io.getarrays.securecapita.domain.HttpResponse;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/MailingList")
@RequiredArgsConstructor
public class MailingListController {
private  MailingListService mailingListService;

    @PostMapping
    public ResponseEntity<?> createMailingList(@RequestBody MailingList mailingList) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mailingListService.save(mailingList));
    }



}
