package io.getarrays.securecapita.PurchaseRequest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum PurchaseRequestStatusEnum {

    RECEIVED(null, List.of("ADMINOFFICER")),
    COMPLETED(RECEIVED, List.of("HEADADMIN")),
    CONFIRMED(COMPLETED, List.of("DEPUTYHEADADMIN")),
    APPROVED(CONFIRMED, List.of("PRINCIPAL_ADMIN")),
    INITIATED(APPROVED, List.of("ADMINOFFICER")),
    REJECTED(null, List.of("ADMINOFFICER", "PRINCIPAL_ADMIN", "DEPUTYHEADADMIN", "HEADADMIN"));

    private final PurchaseRequestStatusEnum next;

    private final List<String> roles;
}
