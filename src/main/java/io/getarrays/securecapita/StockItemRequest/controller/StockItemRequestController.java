package io.getarrays.securecapita.StockItemRequest.controller;


import io.getarrays.securecapita.StockItemRequest.domain.StockItemRequest;
import io.getarrays.securecapita.StockItemRequest.domain.StockItemRequestProduct;
import io.getarrays.securecapita.StockItemRequest.service.StockItemRequestService;
import io.getarrays.securecapita.domain.HttpResponse;
import io.getarrays.securecapita.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/StockItemRequest")
@RequiredArgsConstructor

public class StockItemRequestController {

    @Autowired
    StockItemRequestService stockItemRequestService;

    @PostMapping("/create")
    public StockItemRequest createStockItemRequest(@RequestBody StockItemRequest stockItemRequest) {
        System.out.println("Incoming stock item request from the client is: " + stockItemRequest);

        StockItemRequest createdStockItemRequest = stockItemRequestService.createStockItemRequest(stockItemRequest);

        System.out.println("Created stock item request is: " + createdStockItemRequest);

        return createdStockItemRequest;
    }




    @GetMapping("/get")
    public List<StockItemRequest> getAllStockItemRequests() {
        List<StockItemRequest> listOfAllStockItemRequests = stockItemRequestService.getAllStockItemRequests();
        return listOfAllStockItemRequests;
    }

    @DeleteMapping("/delete/{Id}")
    public String deleteStockItemRequest(@PathVariable("id") Long stockItemRequestId) {

       stockItemRequestService.deleteStockItemRequest(stockItemRequestId);

        return "Stock item request deleted successfully";
    }

//    @PutMapping("/update/{Id}")
//    public StockItemRequest updateStockItemRequest(@PathVariable("id") Long stockItemRequestId, @RequestBody StockItemRequest stockItemRequest) {
//        StockItemRequest oldStockItemRequest = stockItemRequestService.updateStockItemRequest(stockItemRequestId);
//
//        // Update relevant fields from the received request
//        oldStockItemRequest.setQuantity(stockItemRequest.getQuantity()); // Example: Update quantity field
//        // Update other relevant fields based on your StockItemRequest class
//
//        StockItemRequest updatedStockItemRequest = stockItemRequestService.updateStockItemRequest(oldStockItemRequest.getId());
//
//        return updatedStockItemRequest;
//    }





    @PostMapping("/stockItemRequestProduct/add/{id}")
    public ResponseEntity<HttpResponse> addProductToRequest(@AuthenticationPrincipal UserDTO user, @PathVariable("id") Long id, @RequestBody StockItemRequestProduct stockItemRequestProduct) {
        try {
            stockItemRequestService.addStockItemRequestProducttoStockItemRequest(id, stockItemRequestProduct);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .data(of("stockItemRequests", stockItemRequestService.getAllStockItemRequests()))
                            .message(String.format("Product added to request with ID: %s", id))
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .build());
        } catch (Exception e) {
            // Handle or log the exception as appropriate
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
