package io.getarrays.securecapita.assertmoverequests;

import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/station/asset/move/requests")
@AllArgsConstructor
public class AssertMoveController {
    private final AssertMoveService assertMoveService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestParam(name = "page",defaultValue = "0")int page,@RequestParam(name = "size",defaultValue = "10")int size,@RequestParam(name = "stationId",defaultValue = "0")long stationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.REQUEST_MOVE_ASSET.name()))||(authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.APPROVE_MOVE_ASSET.name()))))) {
            return assertMoveService.getAll(page, size,stationId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }

    @PostMapping("/request")
    public ResponseEntity<Object> addRequest(@RequestBody @Validated AssertMoveRequestDto assertMoveRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.REQUEST_MOVE_ASSET.name()))||(authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.APPROVE_MOVE_ASSET.name()))))) {
            return assertMoveService.addRequest(assertMoveRequestDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }

    @PostMapping("/approve")
    public ResponseEntity<Object> addLocation(@RequestParam("assertMoveRequestId") Long assertRequestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.APPROVE_MOVE_ASSET.name()))) {
            return assertMoveService.approve(assertRequestId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }

    @PostMapping("/reject")
    public ResponseEntity<Object> rejectLocation(@RequestParam("assertMoveRequestId") Long assertRequestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.APPROVE_MOVE_ASSET.name()))) {
            return assertMoveService.reject(assertRequestId);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }
}
