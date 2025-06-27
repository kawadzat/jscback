package io.getarrays.securecapita.resource;

import io.getarrays.securecapita.domain.Stock;
import io.getarrays.securecapita.report.StockReport;
import io.getarrays.securecapita.repository.StockRepository;
import io.getarrays.securecapita.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.parseMediaType;
@RestController
@RequestMapping(path = "/stock")
@RequiredArgsConstructor
public class StockResource {


    // Assume ProductService class handles product inventory
    @Autowired
    private StockService stockService;



    @PostMapping("/add")
    public ResponseEntity<Stock> createProduct(@RequestBody Stock stock){

        Stock createStock =  stockService.createStock(stock);
        return new ResponseEntity<>(createStock, HttpStatus.OK);
    }



//    @GetMapping("/check/{productCode}")
//    public ResponseEntity<String> checkProduct(@PathVariable String productCode) {
//        boolean productExists = stockService.findProductByCode(productCode);
//        if (productExists) {
//            return new ResponseEntity<>("Product is in stock", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Product is not in stock", HttpStatus.OK);
//        }
//    }



    @GetMapping("/all")
    public ResponseEntity<List<Stock>> getAllStock() {
        List<Stock> allStock = stockService.getAllStock();
        return new ResponseEntity<>(allStock, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteStock(@PathVariable Long productId) {
        boolean deleted = stockService.deleteStock(productId);
        if (deleted) {
            return new ResponseEntity<>("Stock deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete stock", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/check/{productCode}")
    public ResponseEntity<String> checkProduct(@PathVariable String productCode) {
        boolean productExists = stockService.isProductInStock(productCode);
        if (productExists) {
            return new ResponseEntity<>("Product is in stock", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product is not in stock", HttpStatus.OK);
        }
    }
    @GetMapping("/totalQuantity/{productCode}")
    public ResponseEntity<Integer> getTotalQuantityByProductCode(@PathVariable String productCode) {
        int totalQuantity = stockService.getTotalQuantityByProductCode(productCode);
        return ResponseEntity.ok(totalQuantity);
    }


    @GetMapping("/download/report")
    public ResponseEntity<Resource> downloadReport() {
        List<Stock> stock = new ArrayList<>();
        stockService.getAllStock().iterator().forEachRemaining(stock::add);
        StockReport report = new StockReport(stock);
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", "customer-report.xlsx");
        headers.add(CONTENT_DISPOSITION, "attachment;File-Name=stock-report.xlsx");
        return ResponseEntity.ok().contentType(parseMediaType("application/vnd.ms-excel"))
                .headers(headers).body(report.export());
    }



}
