package io.getarrays.securecapita.bills;

import io.getarrays.securecapita.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("water-bills")
@RequiredArgsConstructor
@Slf4j
public class WaterBillController {

    private final WaterBillService waterBillService;

    // Basic CRUD endpoints
    @PostMapping
    public ResponseEntity<WaterBill> createWaterBill(@RequestBody WaterBill waterBill) {
        WaterBill createdBill = waterBillService.createWaterBill(waterBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaterBill> getWaterBillById(@PathVariable Long id) {
        WaterBill waterBill = waterBillService.getWaterBillById(id);
        return ResponseEntity.ok(waterBill);
    }

    @GetMapping("/bill-number/{billNumber}")
    public ResponseEntity<WaterBill> getWaterBillByBillNumber(@PathVariable String billNumber) {
        WaterBill waterBill = waterBillService.getWaterBillByBillNumber(billNumber);
        return ResponseEntity.ok(waterBill);
    }

    @GetMapping
    public ResponseEntity<List<WaterBill>> getAllWaterBills() {
        List<WaterBill> waterBills = waterBillService.getAllWaterBills();
        return ResponseEntity.ok(waterBills);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaterBill> updateWaterBill(@PathVariable Long id, @RequestBody WaterBill waterBill) {
        WaterBill updatedBill = waterBillService.updateWaterBill(id, waterBill);
        return ResponseEntity.ok(updatedBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterBill(@PathVariable Long id) {
        waterBillService.deleteWaterBill(id);
        return ResponseEntity.noContent().build();
    }

    // Customer Address specific endpoints
    @PostMapping("/customer-address/{customerAddressId}")
    public ResponseEntity<WaterBill> createWaterBillForCustomerAddress(@PathVariable Long customerAddressId, @RequestBody WaterBill waterBill) {
        WaterBill createdBill = waterBillService.createWaterBillForCustomerAddress(customerAddressId, waterBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @PostMapping("/customer-address/phone/{customerPhone}")
    public ResponseEntity<WaterBill> createWaterBillForCustomerAddressByPhone(
            @PathVariable String customerPhone, 
            @RequestParam String addressType, 
            @RequestBody WaterBill waterBill) {
        WaterBill createdBill = waterBillService.createWaterBillForCustomerAddressByPhone(customerPhone, addressType, waterBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @PostMapping("/customer-address/whatsapp/{customerWhatsapp}")
    public ResponseEntity<WaterBill> createWaterBillForCustomerAddressByWhatsapp(
            @PathVariable String customerWhatsapp, 
            @RequestParam String addressType, 
            @RequestBody WaterBill waterBill) {
        WaterBill createdBill = waterBillService.createWaterBillForCustomerAddressByWhatsapp(customerWhatsapp, addressType, waterBill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @PostMapping("/{id}/send/customer-address-whatsapp")
    public ResponseEntity<Map<String, Object>> sendWaterBillToCustomerAddressWhatsApp(@PathVariable Long id) {
        boolean success = waterBillService.sendWaterBillToCustomerAddressWhatsApp(id);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent to customer address WhatsApp successfully" : "Failed to send water bill to customer address WhatsApp",
            "billId", id
        ));
    }

    @PostMapping("/bill-number/{billNumber}/send/customer-address-whatsapp")
    public ResponseEntity<Map<String, Object>> sendWaterBillToCustomerAddressWhatsAppByBillNumber(@PathVariable String billNumber) {
        boolean success = waterBillService.sendWaterBillToCustomerAddressWhatsApp(billNumber);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent to customer address WhatsApp successfully" : "Failed to send water bill to customer address WhatsApp",
            "billNumber", billNumber
        ));
    }

    @PostMapping("/customer-address/{customerAddressId}/send")
    public ResponseEntity<Map<String, Object>> sendWaterBillToCustomerAddress(
            @PathVariable Long customerAddressId, 
            @RequestParam DeliveryMethod deliveryMethod) {
        boolean success = waterBillService.sendWaterBillToCustomerAddress(customerAddressId, deliveryMethod);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bills sent to customer address successfully" : "Failed to send water bills to customer address",
            "customerAddressId", customerAddressId,
            "deliveryMethod", deliveryMethod
        ));
    }

    @GetMapping("/customer-address/{customerAddressId}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByCustomerAddress(@PathVariable Long customerAddressId) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByCustomerAddress(customerAddressId);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/customer-address/phone/{customerPhone}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByCustomerAddressByPhone(
            @PathVariable String customerPhone, 
            @RequestParam String addressType) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByCustomerAddressByPhone(customerPhone, addressType);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/customer-address/whatsapp/{customerWhatsapp}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByCustomerAddressByWhatsapp(
            @PathVariable String customerWhatsapp, 
            @RequestParam String addressType) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByCustomerAddressByWhatsapp(customerWhatsapp, addressType);
        return ResponseEntity.ok(waterBills);
    }

    // Generate bill for customer address
    @PostMapping("/customer-address/{customerAddressId}/generate")
    public ResponseEntity<WaterBill> generateWaterBillForCustomerAddress(
            @PathVariable Long customerAddressId,
            @RequestBody Map<String, Object> request) {
        WaterBill waterBill = waterBillService.generateWaterBillForCustomerAddress(
            customerAddressId,
            (String) request.get("meterNumber"),
            new BigDecimal(request.get("previousReading").toString()),
            new BigDecimal(request.get("currentReading").toString()),
            new BigDecimal(request.get("ratePerUnit").toString()),
            LocalDate.parse(request.get("billingPeriodStart").toString()),
            LocalDate.parse(request.get("billingPeriodEnd").toString())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(waterBill);
    }

    // Bill delivery endpoints
    @PostMapping("/{id}/send/whatsapp")
    public ResponseEntity<Map<String, Object>> sendWaterBillToWhatsApp(@PathVariable Long id) {
        boolean success = waterBillService.sendWaterBillToWhatsApp(id);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent to WhatsApp successfully" : "Failed to send water bill to WhatsApp",
            "billId", id
        ));
    }

    @PostMapping("/bill-number/{billNumber}/send/whatsapp")
    public ResponseEntity<Map<String, Object>> sendWaterBillToWhatsAppByBillNumber(@PathVariable String billNumber) {
        boolean success = waterBillService.sendWaterBillToWhatsApp(billNumber);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent to WhatsApp successfully" : "Failed to send water bill to WhatsApp",
            "billNumber", billNumber
        ));
    }

    @PostMapping("/{id}/send/address")
    public ResponseEntity<Map<String, Object>> sendWaterBillToAddress(@PathVariable Long id) {
        boolean success = waterBillService.sendWaterBillToAddress(id);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent to address successfully" : "Failed to send water bill to address",
            "billId", id
        ));
    }

    @PostMapping("/bill-number/{billNumber}/send/address")
    public ResponseEntity<Map<String, Object>> sendWaterBillToAddressByBillNumber(@PathVariable String billNumber) {
        boolean success = waterBillService.sendWaterBillToAddress(billNumber);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent to address successfully" : "Failed to send water bill to address",
            "billNumber", billNumber
        ));
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<Map<String, Object>> sendWaterBill(@PathVariable Long id, 
                                                           @RequestParam DeliveryMethod deliveryMethod) {
        boolean success = waterBillService.sendWaterBill(id, deliveryMethod);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Water bill sent successfully" : "Failed to send water bill",
            "billId", id,
            "deliveryMethod", deliveryMethod
        ));
    }

    @PostMapping("/bulk/send")
    public ResponseEntity<Map<String, Object>> sendBulkWaterBills(@RequestBody List<Long> billIds,
                                                                 @RequestParam DeliveryMethod deliveryMethod) {
        boolean success = waterBillService.sendBulkWaterBills(billIds, deliveryMethod);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Bulk water bills sent successfully" : "Failed to send some water bills",
            "billIds", billIds,
            "deliveryMethod", deliveryMethod
        ));
    }

    @PostMapping("/bulk/send/status")
    public ResponseEntity<Map<String, Object>> sendBulkWaterBillsByStatus(@RequestParam WaterBillStatus status,
                                                                         @RequestParam DeliveryMethod deliveryMethod) {
        boolean success = waterBillService.sendBulkWaterBillsByStatus(status, deliveryMethod);
        return ResponseEntity.ok(Map.of(
            "success", success,
            "message", success ? "Bulk water bills sent successfully" : "Failed to send some water bills",
            "status", status,
            "deliveryMethod", deliveryMethod
        ));
    }

    // Search and filter endpoints
    @GetMapping("/customer-phone/{phone}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByCustomerPhone(@PathVariable String phone) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByCustomerPhone(phone);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/whatsapp-number/{whatsappNumber}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByWhatsAppNumber(@PathVariable String whatsappNumber) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByWhatsAppNumber(whatsappNumber);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/meter-number/{meterNumber}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByMeterNumber(@PathVariable String meterNumber) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByMeterNumber(meterNumber);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByStatus(@PathVariable WaterBillStatus status) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByStatus(status);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/delivery-method/{deliveryMethod}")
    public ResponseEntity<List<WaterBill>> getWaterBillsByDeliveryMethod(@PathVariable DeliveryMethod deliveryMethod) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByDeliveryMethod(deliveryMethod);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/customer-name")
    public ResponseEntity<List<WaterBill>> getWaterBillsByCustomerName(@RequestParam String customerName) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByCustomerName(customerName);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/home-address")
    public ResponseEntity<List<WaterBill>> getWaterBillsByHomeAddress(@RequestParam String address) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByHomeAddress(address);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<WaterBill>> getOverdueWaterBills() {
        List<WaterBill> waterBills = waterBillService.getOverdueWaterBills();
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/billing-period")
    public ResponseEntity<List<WaterBill>> getWaterBillsByBillingPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsByBillingPeriod(startDate, endDate);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/due-date")
    public ResponseEntity<List<WaterBill>> getWaterBillsDueOnDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsDueOnDate(date);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/due-between")
    public ResponseEntity<List<WaterBill>> getWaterBillsDueBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsDueBetweenDates(startDate, endDate);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/high-consumption")
    public ResponseEntity<List<WaterBill>> getWaterBillsWithHighConsumption(@RequestParam BigDecimal threshold) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsWithHighConsumption(threshold);
        return ResponseEntity.ok(waterBills);
    }

    @GetMapping("/high-amount")
    public ResponseEntity<List<WaterBill>> getWaterBillsWithHighAmount(@RequestParam BigDecimal amount) {
        List<WaterBill> waterBills = waterBillService.getWaterBillsWithHighAmount(amount);
        return ResponseEntity.ok(waterBills);
    }

    // Business logic endpoints
    @PostMapping("/generate")
    public ResponseEntity<WaterBill> generateWaterBill(@RequestBody Map<String, Object> request) {
        WaterBill waterBill = waterBillService.generateWaterBill(
            (String) request.get("customerName"),
            (String) request.get("customerPhone"),
            (String) request.get("whatsappNumber"),
            (String) request.get("homeAddress"),
            (String) request.get("meterNumber"),
            new BigDecimal(request.get("previousReading").toString()),
            new BigDecimal(request.get("currentReading").toString()),
            new BigDecimal(request.get("ratePerUnit").toString()),
            LocalDate.parse(request.get("billingPeriodStart").toString()),
            LocalDate.parse(request.get("billingPeriodEnd").toString())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(waterBill);
    }

    @PostMapping("/{id}/calculate")
    public ResponseEntity<WaterBill> calculateBillAmounts(@PathVariable Long id) {
        WaterBill waterBill = waterBillService.calculateBillAmounts(id);
        return ResponseEntity.ok(waterBill);
    }

    @PostMapping("/bill-number/{billNumber}/calculate")
    public ResponseEntity<WaterBill> calculateBillAmountsByBillNumber(@PathVariable String billNumber) {
        WaterBill waterBill = waterBillService.calculateBillAmounts(billNumber);
        return ResponseEntity.ok(waterBill);
    }

    @PostMapping("/{id}/mark-paid")
    public ResponseEntity<WaterBill> markAsPaid(@PathVariable Long id) {
        WaterBill waterBill = waterBillService.markAsPaid(id);
        return ResponseEntity.ok(waterBill);
    }

    @PostMapping("/bill-number/{billNumber}/mark-paid")
    public ResponseEntity<WaterBill> markAsPaidByBillNumber(@PathVariable String billNumber) {
        WaterBill waterBill = waterBillService.markAsPaid(billNumber);
        return ResponseEntity.ok(waterBill);
    }

    @PostMapping("/{id}/mark-overdue")
    public ResponseEntity<WaterBill> markAsOverdue(@PathVariable Long id) {
        WaterBill waterBill = waterBillService.markAsOverdue(id);
        return ResponseEntity.ok(waterBill);
    }

    @PostMapping("/bill-number/{billNumber}/mark-overdue")
    public ResponseEntity<WaterBill> markAsOverdueByBillNumber(@PathVariable String billNumber) {
        WaterBill waterBill = waterBillService.markAsOverdue(billNumber);
        return ResponseEntity.ok(waterBill);
    }

    // Reporting endpoints
    @GetMapping("/stats/count-by-status/{status}")
    public ResponseEntity<Map<String, Object>> countWaterBillsByStatus(@PathVariable WaterBillStatus status) {
        long count = waterBillService.countWaterBillsByStatus(status);
        return ResponseEntity.ok(Map.of("status", status, "count", count));
    }

    @GetMapping("/stats/count-by-delivery-method/{deliveryMethod}")
    public ResponseEntity<Map<String, Object>> countWaterBillsByDeliveryMethod(@PathVariable DeliveryMethod deliveryMethod) {
        long count = waterBillService.countWaterBillsByDeliveryMethod(deliveryMethod);
        return ResponseEntity.ok(Map.of("deliveryMethod", deliveryMethod, "count", count));
    }

    @GetMapping("/stats/overdue-count")
    public ResponseEntity<Map<String, Object>> countOverdueWaterBills() {
        long count = waterBillService.countOverdueWaterBills();
        return ResponseEntity.ok(Map.of("overdueCount", count));
    }

    @GetMapping("/stats/sum-by-status/{status}")
    public ResponseEntity<Map<String, Object>> sumTotalAmountByStatus(@PathVariable WaterBillStatus status) {
        BigDecimal sum = waterBillService.sumTotalAmountByStatus(status);
        return ResponseEntity.ok(Map.of("status", status, "totalAmount", sum));
    }

    @GetMapping("/stats/sum-by-delivery-method/{deliveryMethod}")
    public ResponseEntity<Map<String, Object>> sumTotalAmountByDeliveryMethod(@PathVariable DeliveryMethod deliveryMethod) {
        BigDecimal sum = waterBillService.sumTotalAmountByDeliveryMethod(deliveryMethod);
        return ResponseEntity.ok(Map.of("deliveryMethod", deliveryMethod, "totalAmount", sum));
    }

    @GetMapping("/stats/outstanding-amount")
    public ResponseEntity<Map<String, Object>> getTotalOutstandingAmount() {
        BigDecimal amount = waterBillService.getTotalOutstandingAmount();
        return ResponseEntity.ok(Map.of("outstandingAmount", amount));
    }

    @GetMapping("/stats/paid-amount")
    public ResponseEntity<Map<String, Object>> getTotalPaidAmount() {
        BigDecimal amount = waterBillService.getTotalPaidAmount();
        return ResponseEntity.ok(Map.of("paidAmount", amount));
    }

    @GetMapping("/stats/average-bill-amount")
    public ResponseEntity<Map<String, Object>> getAverageBillAmount() {
        BigDecimal amount = waterBillService.getAverageBillAmount();
        return ResponseEntity.ok(Map.of("averageBillAmount", amount));
    }

    @GetMapping("/stats/total-consumption")
    public ResponseEntity<Map<String, Object>> getTotalConsumption() {
        BigDecimal consumption = waterBillService.getTotalConsumption();
        return ResponseEntity.ok(Map.of("totalConsumption", consumption));
    }

    // Validation endpoints
    @GetMapping("/validate/bill-number/{billNumber}")
    public ResponseEntity<Map<String, Object>> isBillNumberExists(@PathVariable String billNumber) {
        boolean exists = waterBillService.isBillNumberExists(billNumber);
        return ResponseEntity.ok(Map.of("billNumber", billNumber, "exists", exists));
    }

    @GetMapping("/validate/meter-number/{meterNumber}")
    public ResponseEntity<Map<String, Object>> isMeterNumberExists(@PathVariable String meterNumber) {
        boolean exists = waterBillService.isMeterNumberExists(meterNumber);
        return ResponseEntity.ok(Map.of("meterNumber", meterNumber, "exists", exists));
    }

    @GetMapping("/validate/whatsapp-number/{whatsappNumber}")
    public ResponseEntity<Map<String, Object>> isValidWhatsAppNumber(@PathVariable String whatsappNumber) {
        boolean valid = waterBillService.isValidWhatsAppNumber(whatsappNumber);
        return ResponseEntity.ok(Map.of("whatsappNumber", whatsappNumber, "valid", valid));
    }

    @GetMapping("/validate/phone-number/{phoneNumber}")
    public ResponseEntity<Map<String, Object>> isValidPhoneNumber(@PathVariable String phoneNumber) {
        boolean valid = waterBillService.isValidPhoneNumber(phoneNumber);
        return ResponseEntity.ok(Map.of("phoneNumber", phoneNumber, "valid", valid));
    }

    @GetMapping("/validate/overdue/{id}")
    public ResponseEntity<Map<String, Object>> isBillOverdue(@PathVariable Long id) {
        boolean overdue = waterBillService.isBillOverdue(id);
        return ResponseEntity.ok(Map.of("billId", id, "overdue", overdue));
    }

    @GetMapping("/validate/overdue/bill-number/{billNumber}")
    public ResponseEntity<Map<String, Object>> isBillOverdueByBillNumber(@PathVariable String billNumber) {
        boolean overdue = waterBillService.isBillOverdue(billNumber);
        return ResponseEntity.ok(Map.of("billNumber", billNumber, "overdue", overdue));
    }

    // Utility endpoints
    @GetMapping("/generate-bill-number")
    public ResponseEntity<Map<String, Object>> generateBillNumber() {
        String billNumber = waterBillService.generateBillNumber();
        return ResponseEntity.ok(Map.of("billNumber", billNumber));
    }

    @GetMapping("/{id}/format/whatsapp")
    public ResponseEntity<Map<String, Object>> formatBillForWhatsApp(@PathVariable Long id) {
        WaterBill waterBill = waterBillService.getWaterBillById(id);
        String formattedMessage = waterBillService.formatBillForWhatsApp(waterBill);
        return ResponseEntity.ok(Map.of(
            "billId", id,
            "formattedMessage", formattedMessage
        ));
    }

    @GetMapping("/{id}/format/address")
    public ResponseEntity<Map<String, Object>> formatBillForAddress(@PathVariable Long id) {
        WaterBill waterBill = waterBillService.getWaterBillById(id);
        String formattedMessage = waterBillService.formatBillForAddress(waterBill);
        return ResponseEntity.ok(Map.of(
            "billId", id,
            "formattedMessage", formattedMessage
        ));
    }

    // Error handling
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Not Found", "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Bad Request", "message", ex.getMessage()));
    }
} 