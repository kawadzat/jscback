package io.getarrays.securecapita.StockItemRequest.service;




import com.fasterxml.jackson.annotation.JsonInclude;
import io.getarrays.securecapita.StockItemRequest.domain.StockItemRequest;
import io.getarrays.securecapita.StockItemRequest.domain.StockItemRequestProduct;
import io.getarrays.securecapita.StockItemRequest.repository.StockItemRequestProductRepo;
import io.getarrays.securecapita.StockItemRequest.repository.StockItemRequestRepository;
import io.getarrays.securecapita.asserts.model.AssertEntity;
import io.getarrays.securecapita.asserts.model.Inspection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StockItemRequestService implements StockItemRequestServiceInterface {

    @Autowired
    StockItemRequestProductRepo stockItemRequestProductRepo;


    @Autowired
    StockItemRequestRepository stockItemRequestRepository;

    public StockItemRequestService(StockItemRequestRepository stockItemRequestRepository) {
        this.stockItemRequestRepository = stockItemRequestRepository;
    }

    public StockItemRequest createStockItemRequest(StockItemRequest stockItemRequest) {

        StockItemRequest createdStockItemRequest = stockItemRequestRepository.save(stockItemRequest);

        return createdStockItemRequest;
    }

    public List<StockItemRequest> getAllStockItemRequests() {
        List<StockItemRequest> allStockItemRequests = stockItemRequestRepository.findAll();
        return allStockItemRequests;
    }

    public void deleteStockItemRequest(long stockItemRequestId) {
        stockItemRequestRepository.deleteById(stockItemRequestId);
    }

    @Override
    public void addStockItemRequestProducttoStockItemRequest(Long id, StockItemRequestProduct stockItemRequestProduct) {
        Optional<StockItemRequest> stockItemRequestOptional = stockItemRequestRepository.findById(id);

        if (stockItemRequestOptional.isPresent()) {
            StockItemRequest stockItemRequest = stockItemRequestOptional.get();

            // Correctly set the StockItemRequest on the StockItemRequestProduct
            stockItemRequestProduct.setStockItemRequest(stockItemRequest);

            // Save the updated StockItemRequestProduct
            stockItemRequestProductRepo.save(stockItemRequestProduct);
        } else {
            // Handle the case where the StockItemRequest is not found
            throw new EntityNotFoundException("StockItemRequest with ID " + id + " not found.");
        }
    }






}