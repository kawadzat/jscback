package io.getarrays.securecapita.productsrequests.repository;

import io.getarrays.securecapita.productsrequests.entity.ProductRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRequestRepository extends JpaRepository<ProductRequestEntity, Long> {
}
