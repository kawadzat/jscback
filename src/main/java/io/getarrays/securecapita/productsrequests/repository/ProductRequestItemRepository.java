package io.getarrays.securecapita.productsrequests.repository;


import io.getarrays.securecapita.productsrequests.entity.ProductRequestItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRequestItemRepository extends JpaRepository<ProductRequestItemEntity, Long> {

}
