package io.getarrays.securecapita.PurchaseRequest.service;

import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestApprovalDto;
import io.getarrays.securecapita.PurchaseRequest.dto.PurchaseRequestDto;
import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestEntity;
import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestItemEntity;
import io.getarrays.securecapita.domain.PageResponseDto;
import io.getarrays.securecapita.dto.UserDTO;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface PurchaseRequestServiceInterface {
    PurchaseRequestDto getPurchaseRequestById(Long purchaseRequestId);

    PageResponseDto<PurchaseRequestDto> getAllPurchaseRequests(PageRequest pageRequest);

    void deletePurchaseRequest(Long purchaseRequestId);

    void addProductsToPurchaseRequest(Long id, PurchaseRequestItemEntity purchaseRequestProduct);

    void addPurchaseRequestProducttoPurchaseRequest(Long id, PurchaseRequestItemEntity purchaseRequestProduct);

    Optional<PurchaseRequestEntity> findById(Long id);

    PurchaseRequestDto createPurchaseRequest(UserDTO currentUser, PurchaseRequestDto purchaseRequestDto);

    boolean approvePurchaseRequest(UserDTO currentUser, Long purchaseRequestId, PurchaseRequestApprovalDto purchaseRequestApprovalDto) throws Exception;
}
