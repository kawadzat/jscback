package io.getarrays.securecapita.bills;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface WaterBillService {

    // Basic CRUD operations
    WaterBill createWaterBill(WaterBill waterBill);
    WaterBill getWaterBillById(Long id);
    WaterBill getWaterBillByBillNumber(String billNumber);
    List<WaterBill> getAllWaterBills();
    WaterBill updateWaterBill(Long id, WaterBill waterBill);
    void deleteWaterBill(Long id);

    // Bill delivery methods
    boolean sendWaterBillToWhatsApp(Long billId);
    boolean sendWaterBillToWhatsApp(String billNumber);
    boolean sendWaterBillToAddress(Long billId);
    boolean sendWaterBillToAddress(String billNumber);
    boolean sendWaterBill(Long billId, DeliveryMethod deliveryMethod);
    boolean sendWaterBill(String billNumber, DeliveryMethod deliveryMethod);
    boolean sendBulkWaterBills(List<Long> billIds, DeliveryMethod deliveryMethod);
    boolean sendBulkWaterBillsByStatus(WaterBillStatus status, DeliveryMethod deliveryMethod);

    // Customer Address specific methods
    WaterBill createWaterBillForCustomerAddress(Long customerAddressId, WaterBill waterBill);
    WaterBill createWaterBillForCustomerAddressByPhone(String customerPhone, String addressType, WaterBill waterBill);
    WaterBill createWaterBillForCustomerAddressByWhatsapp(String customerWhatsapp, String addressType, WaterBill waterBill);
    boolean sendWaterBillToCustomerAddressWhatsApp(Long billId);
    boolean sendWaterBillToCustomerAddressWhatsApp(String billNumber);
    boolean sendWaterBillToCustomerAddress(Long customerAddressId, DeliveryMethod deliveryMethod);
    List<WaterBill> getWaterBillsByCustomerAddress(Long customerAddressId);
    List<WaterBill> getWaterBillsByCustomerAddressByPhone(String customerPhone, String addressType);
    List<WaterBill> getWaterBillsByCustomerAddressByWhatsapp(String customerWhatsapp, String addressType);

    // Search and filter methods
    List<WaterBill> getWaterBillsByCustomerPhone(String customerPhone);
    List<WaterBill> getWaterBillsByWhatsAppNumber(String whatsappNumber);
    List<WaterBill> getWaterBillsByMeterNumber(String meterNumber);
    List<WaterBill> getWaterBillsByStatus(WaterBillStatus status);
    List<WaterBill> getWaterBillsByDeliveryMethod(DeliveryMethod deliveryMethod);
    List<WaterBill> getWaterBillsByCustomerName(String customerName);
    List<WaterBill> getWaterBillsByHomeAddress(String address);
    List<WaterBill> getOverdueWaterBills();
    List<WaterBill> getWaterBillsByBillingPeriod(LocalDate startDate, LocalDate endDate);
    List<WaterBill> getWaterBillsDueOnDate(LocalDate date);
    List<WaterBill> getWaterBillsDueBetweenDates(LocalDate startDate, LocalDate endDate);
    List<WaterBill> getWaterBillsWithHighConsumption(BigDecimal threshold);
    List<WaterBill> getWaterBillsWithHighAmount(BigDecimal amount);

    // Business logic methods
    WaterBill generateWaterBill(String customerName, String customerPhone, String whatsappNumber, 
                               String homeAddress, String meterNumber, BigDecimal previousReading, 
                               BigDecimal currentReading, BigDecimal ratePerUnit, LocalDate billingPeriodStart, 
                               LocalDate billingPeriodEnd);
    WaterBill generateWaterBillForCustomerAddress(Long customerAddressId, String meterNumber, 
                                                 BigDecimal previousReading, BigDecimal currentReading, 
                                                 BigDecimal ratePerUnit, LocalDate billingPeriodStart, 
                                                 LocalDate billingPeriodEnd);
    WaterBill calculateBillAmounts(Long billId);
    WaterBill calculateBillAmounts(String billNumber);
    WaterBill markAsPaid(Long billId);
    WaterBill markAsPaid(String billNumber);
    WaterBill markAsOverdue(Long billId);
    WaterBill markAsOverdue(String billNumber);
    WaterBill updateDeliveryStatus(Long billId, String status, String message);
    WaterBill updateDeliveryStatus(String billNumber, String status, String message);

    // Reporting methods
    long countWaterBillsByStatus(WaterBillStatus status);
    long countWaterBillsByDeliveryMethod(DeliveryMethod deliveryMethod);
    long countOverdueWaterBills();
    BigDecimal sumTotalAmountByStatus(WaterBillStatus status);
    BigDecimal sumTotalAmountByDeliveryMethod(DeliveryMethod deliveryMethod);
    BigDecimal getTotalOutstandingAmount();
    BigDecimal getTotalPaidAmount();
    BigDecimal getAverageBillAmount();
    BigDecimal getTotalConsumption();

    // Validation methods
    boolean isBillNumberExists(String billNumber);
    boolean isMeterNumberExists(String meterNumber);
    boolean isValidWhatsAppNumber(String whatsappNumber);
    boolean isValidPhoneNumber(String phoneNumber);
    boolean isBillOverdue(Long billId);
    boolean isBillOverdue(String billNumber);

    // Utility methods
    String generateBillNumber();
    String formatBillForWhatsApp(WaterBill waterBill);
    String formatBillForAddress(WaterBill waterBill);
    String formatBillForEmail(WaterBill waterBill);
    String formatBillForSMS(WaterBill waterBill);
} 