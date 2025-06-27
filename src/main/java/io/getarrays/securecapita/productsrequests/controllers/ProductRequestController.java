package io.getarrays.securecapita.productsrequests.controllers;

import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.productsrequests.dto.ProductRequestDto;
import io.getarrays.securecapita.productsrequests.service.ProductRequestService;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product/request")
@RequiredArgsConstructor
public class ProductRequestController {
    private final ProductRequestService productRequestService;

    @PostMapping("/request")
    public ResponseEntity<?> requestProduct(@Validated @RequestBody ProductRequestDto requestMasterDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.CREATE_PURCHASEREQUEST.name()))) {
            return productRequestService.requestProduct(requestMasterDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return productRequestService.getAllRequests(page, size);
    }
}
