package io.getarrays.securecapita.PurchaseRequest.service;


import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestItemEntity;
import io.getarrays.securecapita.PurchaseRequest.repository.PurchaseRequestProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public class PurchaseRequestProductService {

    @Autowired
    PurchaseRequestProductRepo purchaseRequestProductRepo;

    public PurchaseRequestProductService(PurchaseRequestProductRepo purchaseRequestProductRepo) {
        this.purchaseRequestProductRepo = purchaseRequestProductRepo;
    }

    public PurchaseRequestItemEntity createPurchaseRequestProduct(PurchaseRequestItemEntity purchaseRequestProduct) {

        PurchaseRequestItemEntity createdPurchaseRequestProduct = purchaseRequestProductRepo.save(purchaseRequestProduct);

        return createdPurchaseRequestProduct;
    }

    public List<PurchaseRequestItemEntity> getAllPurchaseRequestsProducts() {
        List<PurchaseRequestItemEntity> allPurchaseRequests = purchaseRequestProductRepo.findAll();
        return allPurchaseRequests;
    }








    public void deletePurchaseRequestProduct(Long purchaseRequestProductId) {
        purchaseRequestProductRepo.deleteById(purchaseRequestProductId);
    }


}
