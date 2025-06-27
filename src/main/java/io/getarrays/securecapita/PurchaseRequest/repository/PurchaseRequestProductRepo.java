package io.getarrays.securecapita.PurchaseRequest.repository;

import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface PurchaseRequestProductRepo extends JpaRepository<PurchaseRequestItemEntity,Long> {

//    PurchaseRequestProduct findBypurchaseRequestProductNumber();
}
