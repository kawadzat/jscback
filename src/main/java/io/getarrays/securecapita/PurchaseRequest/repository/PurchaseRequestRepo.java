package io.getarrays.securecapita.PurchaseRequest.repository;


import io.getarrays.securecapita.PurchaseRequest.entity.PurchaseRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PurchaseRequestRepo extends JpaRepository<PurchaseRequestEntity,Long> {
  //     PurchaseRequest findBypurchaseRequestNumber();


}
