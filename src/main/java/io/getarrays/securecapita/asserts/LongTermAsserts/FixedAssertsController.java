package io.getarrays.securecapita.asserts.LongTermAsserts;

import io.getarrays.securecapita.asserts.dto.AssertDto;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/fixedassert/fixedasserts")
@RequiredArgsConstructor
public class FixedAssertsController {
private FixedAssertsService fixedAssertsService;


    @PostMapping("/create")
    public ResponseEntity<?> createAssert(@RequestBody @Validated FixedAssertDto newFixedAssert) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.CREATE_ASSET.name()))) {
            return fixedAssertsService.createFixedAssert(newFixedAssert);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));

    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllFixedAsserts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.ALL_STATION.name()))) {
            return ResponseEntity.ok(fixedAssertsService.getFixedAsserts(page, size));
        }
//        else if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.VIEW_ASSET.name()))) {
//            return assertService.getAssertsForOwnStation(page, size);
//        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }




}
