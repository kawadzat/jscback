package io.getarrays.securecapita.PurchaseRequest.controller;

import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestApprovalDto;
import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestDto;
import io.getarrays.securecapita.PurchaseRequest.service.PurchaseRequestServiceInterface;
import io.getarrays.securecapita.PurchaseRequest.service.PurchaseRequestServiceReport;
import io.getarrays.securecapita.dto.UserDTO;
import io.getarrays.securecapita.exception.CustomMessage;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping(path = "/PurchaseRequest")
@RequiredArgsConstructor

public class PurchaseRequestController {
    @Autowired
    PurchaseRequestServiceInterface purchaseRequestService;

    PurchaseRequestServiceReport purchaseRequestServiceReport;

    @PostMapping("/create")
    public ResponseEntity<CustomMessage> createPurchaseRequest(@AuthenticationPrincipal UserDTO currentUser,
                                                               @RequestBody @Valid PurchaseRequestDto requestDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch((r) -> r.getAuthority().contains(ROLE_AUTH.READ_USER.name()))) {
            return ResponseEntity.ok(new CustomMessage("Purchase Request Created Successfully",
                    purchaseRequestService.createPurchaseRequest(currentUser, requestDto)));

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomMessage("You don't have permission."));
    }

    @GetMapping("/get/{purchaseRequestId}")
    public ResponseEntity<PurchaseRequestDto> getPurchaseRequestById(@PathVariable Long purchaseRequestId) {
        return ResponseEntity.ok().body(purchaseRequestService.getPurchaseRequestById(purchaseRequestId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPurchaseRequests(@RequestParam(name = "page", defaultValue = "0") int page,
                                                    @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok().body(purchaseRequestService.getAllPurchaseRequests(PageRequest.of(page, size,
                Sort.by("lastModifiedDate").descending())));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomMessage> deletePurchaseRequest(@PathVariable("id") Long purchaseRequestId) {

        purchaseRequestService.deletePurchaseRequest(purchaseRequestId);

        return ResponseEntity.ok(new CustomMessage("Purchase Request deleted successfully"));

    }


    @GetMapping("/generate-report")
    public ResponseEntity<Resource> generateReport() {
        // Generate the report as shown in the previous step
        File file = new File("path/to/output/report.pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new FileSystemResource(file));
    }

    @PostMapping("/{purchaseRequestId}/approve")
    public ResponseEntity<?> approve(@PathVariable("purchaseRequestId") Long purchaseRequestId,
                                     @AuthenticationPrincipal UserDTO currentUser,
                                     @RequestBody @Validated PurchaseRequestApprovalDto approvalRequestDto) throws Exception {
        return ResponseEntity.ok(new CustomMessage("Purchase Request approved/rejected Successfully",
                purchaseRequestService.approvePurchaseRequest(currentUser, purchaseRequestId, approvalRequestDto)));
    }
}


