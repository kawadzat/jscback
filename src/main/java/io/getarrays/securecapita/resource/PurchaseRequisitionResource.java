package io.getarrays.securecapita.resource;

import io.getarrays.securecapita.domain.PurchaseRequisition;
import io.getarrays.securecapita.domain.Stock;
import io.getarrays.securecapita.report.PurchaseRequisitionReport;
import io.getarrays.securecapita.service.EmailService;
import io.getarrays.securecapita.service.PurchaseRequisitionService;
import io.getarrays.securecapita.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.parseMediaType;

@RestController
@RequestMapping(path = "/purchaseRequisition")
@RequiredArgsConstructor
public class PurchaseRequisitionResource {

    private final PurchaseRequisitionService purchaseRequisitionService;

    @Autowired
    private EmailService emailService;

//    @PostMapping("/create")
//    public ResponseEntity<PurchaseRequisition> createPurchase(@RequestBody PurchaseRequisition purchaseRequisition) {
//        String email = purchaseRequisition.getReceiverEmail();
//        String subject = "Purchase Request Email Verification Sent By Kumar Kunal";
//        String message = "Hello " + purchaseRequisition.getId() + " for Product Name" + purchaseRequisition.getItemDescription() + ", " +
//                "\n A Purchase Request Email Verification Was Sent To \n" + purchaseRequisition.getReceiverEmail();
//        emailService.sendEmail(email, subject, message);
//        return ResponseEntity.ok(purchaseRequisitionService.createPurchaseRequest(purchaseRequisition)
//        );
//    }

//    @PostMapping("/create")
//    public ResponseEntity<List<PurchaseRequisition>> createPurchase(@RequestBody List<PurchaseRequisition> purchaseRequisitions) {
//        List<PurchaseRequisition> createdPurchaseRequisitions = new ArrayList<>();

//        for (PurchaseRequisition purchaseRequisition : purchaseRequisitions) {
//            String email = purchaseRequisition.getReceiverEmail();
//            String subject = "Purchase Request Email Verification Sent By Kumar Kunal";
//            String message = "Hello " + purchaseRequisition.getId() + " for Product Name " + purchaseRequisition.getItemDescription() + ", " +
//                    "\n A Purchase Request Email Verification Was Sent To \n" + purchaseRequisition.getReceiverEmail();
//            emailService.sendEmail(email, subject, message);

//            PurchaseRequisition createdPurchaseRequisition = purchaseRequisitionService.createPurchaseRequest(purchaseRequisition);
//            createdPurchaseRequisitions.add(createdPurchaseRequisition);
//        }
//
//        return ResponseEntity.ok(createdPurchaseRequisitions);
//    }


    @GetMapping(path = "/{id}")
    public ResponseEntity<PurchaseRequisition> findById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseRequisitionService.getPurchaseRequestById(id));
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<PurchaseRequisition>> findAll() {
        return ResponseEntity.ok(purchaseRequisitionService.getAllPurchaseRequests());
    }

//    @PostMapping(path = "/")
//    public ResponseEntity<PurchaseRequests> save(@RequestBody PurchaseRequests purchaseRequests) {
//        return ResponseEntity.ok(purchaseRequestsService.(purchaseRequests));
//    }



    @GetMapping("/download/report")
    public ResponseEntity<Resource> downloadReport() {
        List<PurchaseRequisition> purchaseRequisitions = new ArrayList<>();
       // customerService.getCustomers().iterator().forEachRemaining(customers::add);

        purchaseRequisitionService.getAllPurchaseRequests().iterator().forEachRemaining(purchaseRequisitions::add);
        PurchaseRequisitionReport report = new PurchaseRequisitionReport(purchaseRequisitions);
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", "PurchaseRequisition-report.xlsx");
        headers.add(CONTENT_DISPOSITION, "attachment;File-Name=PurchaseRequisition-report.xlsx");
        return ResponseEntity.ok().contentType(parseMediaType("application/vnd.ms-excel"))
                .headers(headers).body(report.export());
    }

    @RestController
    @RequestMapping("/product")
    public static class StockController {
        private StockService stockService;

        @PostMapping("/add")
        public ResponseEntity<Stock> createProduct(@RequestBody Stock stock){

            Stock createProduct = stockService.createStock(stock);
            return new ResponseEntity<>(createProduct, HttpStatus.OK);
        }


        @GetMapping("/totalQuantity")
        public int getTotalQuantityByProductCode(@RequestParam String productCode) {
            return stockService.getTotalQuantityByProductCode(productCode);
        }





    }
}
