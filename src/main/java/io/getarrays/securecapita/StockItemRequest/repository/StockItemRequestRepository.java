package io.getarrays.securecapita.StockItemRequest.repository;
import io.getarrays.securecapita.StockItemRequest.domain.StockItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface StockItemRequestRepository extends JpaRepository<StockItemRequest,Long> {
}
