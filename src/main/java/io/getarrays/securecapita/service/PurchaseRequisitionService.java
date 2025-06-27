package io.getarrays.securecapita.service;

import io.getarrays.securecapita.domain.PurchaseRequisition;

import java.util.List;

public interface PurchaseRequisitionService {

    PurchaseRequisition createPurchaseRequest(PurchaseRequisition purchaseRequisition);

    List<PurchaseRequisition> getAllPurchaseRequests();

    PurchaseRequisition getPurchaseRequestById(Long id);

    boolean deletePurchaseRequests(Long id);
}