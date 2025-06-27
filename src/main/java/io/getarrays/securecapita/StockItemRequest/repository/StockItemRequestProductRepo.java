package io.getarrays.securecapita.StockItemRequest.repository;

import io.getarrays.securecapita.StockItemRequest.domain.StockItemRequestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRequestProductRepo extends JpaRepository<StockItemRequestProduct,Long> {
}
