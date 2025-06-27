package io.getarrays.securecapita.service.implementation;

import io.getarrays.securecapita.domain.PurchaseRequisition;
import io.getarrays.securecapita.repository.PurchaseRequestsRepository;
import io.getarrays.securecapita.service.PurchaseRequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestsImpl implements PurchaseRequisitionService {
    private final PurchaseRequestsRepository purchaseRequestsRepository;

    //delete requests
    public boolean deletePurchaseRequest(Long id ){
        return purchaseRequestsRepository.delete(id);
    }

    @Override
    public PurchaseRequisition createPurchaseRequest(PurchaseRequisition purchaseRequests) {
        return purchaseRequestsRepository.create(purchaseRequests);
    }

    @Override
    public List<PurchaseRequisition> getAllPurchaseRequests() {
        return purchaseRequestsRepository.list();
    }

    @Override
    public PurchaseRequisition getPurchaseRequestById(Long id) {
        return (PurchaseRequisition) purchaseRequestsRepository.get(id);
    }

    @Override
    public boolean deletePurchaseRequests(Long id) {
        return purchaseRequestsRepository.delete(id);
    }
}
