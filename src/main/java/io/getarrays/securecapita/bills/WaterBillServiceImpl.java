package io.getarrays.securecapita.bills;

import io.getarrays.securecapita.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WaterBillServiceImpl implements WaterBillService {

    private final WaterBillRepository waterBillRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final TownAddressRepository townAddressRepository;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");
    private static final Pattern WHATSAPP_PATTERN = Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    // Basic CRUD operations
    @Override
    public WaterBill createWaterBill(WaterBill waterBill) {
        if (waterBill.getBillNumber() == null || waterBill.getBillNumber().trim().isEmpty()) {
            waterBill.setBillNumber(generateBillNumber());
        }
        
        if (waterBillRepository.existsByBillNumber(waterBill.getBillNumber())) {
            throw new IllegalArgumentException("Bill number already exists: " + waterBill.getBillNumber());
        }

        // Set default due date if not provided (30 days from billing period end)
        if (waterBill.getDueDate() == null) {
            waterBill.setDueDate(waterBill.getBillingPeriodEnd().plusDays(30));
        }

        // Set default tax if not provided (5% of subtotal)
        if (waterBill.getTax() == null) {
            waterBill.setTax(BigDecimal.ZERO);
        }

        waterBill.calculateFields();
        return waterBillRepository.save(waterBill);
    }

    // Customer Address specific methods
    @Override
    public WaterBill createWaterBillForCustomerAddress(Long customerAddressId, WaterBill waterBill) {
        CustomerAddress customerAddress = customerAddressRepository.findById(customerAddressId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer address not found with id: " + customerAddressId));

        // Populate bill with customer address information
        waterBill.setCustomerName(customerAddress.getCustomerName());
        waterBill.setCustomerPhone(customerAddress.getCustomerPhone());
        waterBill.setWhatsappNumber(customerAddress.getCustomerWhatsapp());
        waterBill.setHomeAddress(customerAddress.getFullAddress());
        waterBill.setCustomerAddress(customerAddress);
        waterBill.setTownAddress(customerAddress.getTownAddress());

        return createWaterBill(waterBill);
    }

    @Override
    public WaterBill createWaterBillForCustomerAddressByPhone(String customerPhone, String addressType, WaterBill waterBill) {
        CustomerAddress customerAddress = customerAddressRepository.findByCustomerPhoneAndAddressType(customerPhone, addressType)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Customer address not found for phone: " + customerPhone + " and type: " + addressType));

        return createWaterBillForCustomerAddress(customerAddress.getId(), waterBill);
    }

    @Override
    public WaterBill createWaterBillForCustomerAddressByWhatsapp(String customerWhatsapp, String addressType, WaterBill waterBill) {
        CustomerAddress customerAddress = customerAddressRepository.findByCustomerWhatsappAndAddressType(customerWhatsapp, addressType)
            .stream()
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Customer address not found for WhatsApp: " + customerWhatsapp + " and type: " + addressType));

        return createWaterBillForCustomerAddress(customerAddress.getId(), waterBill);
    }

    @Override
    public boolean sendWaterBillToCustomerAddressWhatsApp(Long billId) {
        WaterBill waterBill = getWaterBillById(billId);
        
        if (waterBill.getCustomerAddress() == null) {
            log.warn("Water bill {} does not have a customer address linked", billId);
            return sendWaterBillToWhatsApp(billId);
        }

        String whatsappNumber = waterBill.getWhatsAppNumberForDelivery();
        String message = formatBillForWhatsApp(waterBill);
        
        return sendWhatsAppMessage(whatsappNumber, message, waterBill);
    }

    @Override
    public boolean sendWaterBillToCustomerAddressWhatsApp(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return sendWaterBillToCustomerAddressWhatsApp(waterBill.getId());
    }

    @Override
    public boolean sendWaterBillToCustomerAddress(Long customerAddressId, DeliveryMethod deliveryMethod) {
        List<WaterBill> waterBills = getWaterBillsByCustomerAddress(customerAddressId);
        if (waterBills.isEmpty()) {
            log.warn("No water bills found for customer address: {}", customerAddressId);
            return false;
        }

        boolean allSuccess = true;
        for (WaterBill waterBill : waterBills) {
            boolean success = sendWaterBill(waterBill.getId(), deliveryMethod);
            if (!success) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    @Override
    public List<WaterBill> getWaterBillsByCustomerAddress(Long customerAddressId) {
        return waterBillRepository.findByCustomerAddressId(customerAddressId);
    }

    @Override
    public List<WaterBill> getWaterBillsByCustomerAddressByPhone(String customerPhone, String addressType) {
        List<CustomerAddress> customerAddresses = customerAddressRepository.findByCustomerPhoneAndAddressType(customerPhone, addressType);
        return customerAddresses.stream()
            .flatMap(ca -> waterBillRepository.findByCustomerAddressId(ca.getId()).stream())
            .toList();
    }

    @Override
    public List<WaterBill> getWaterBillsByCustomerAddressByWhatsapp(String customerWhatsapp, String addressType) {
        List<CustomerAddress> customerAddresses = customerAddressRepository.findByCustomerWhatsappAndAddressType(customerWhatsapp, addressType);
        return customerAddresses.stream()
            .flatMap(ca -> waterBillRepository.findByCustomerAddressId(ca.getId()).stream())
            .toList();
    }

    @Override
    public WaterBill generateWaterBillForCustomerAddress(Long customerAddressId, String meterNumber, 
                                                        BigDecimal previousReading, BigDecimal currentReading, 
                                                        BigDecimal ratePerUnit, LocalDate billingPeriodStart, 
                                                        LocalDate billingPeriodEnd) {
        CustomerAddress customerAddress = customerAddressRepository.findById(customerAddressId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer address not found with id: " + customerAddressId));

        WaterBill waterBill = new WaterBill();
        waterBill.setBillNumber(generateBillNumber());
        waterBill.setCustomerName(customerAddress.getCustomerName());
        waterBill.setCustomerPhone(customerAddress.getCustomerPhone());
        waterBill.setWhatsappNumber(customerAddress.getCustomerWhatsapp());
        waterBill.setHomeAddress(customerAddress.getFullAddress());
        waterBill.setMeterNumber(meterNumber);
        waterBill.setPreviousReading(previousReading);
        waterBill.setCurrentReading(currentReading);
        waterBill.setRatePerUnit(ratePerUnit);
        waterBill.setBillingPeriodStart(billingPeriodStart);
        waterBill.setBillingPeriodEnd(billingPeriodEnd);
        waterBill.setDueDate(billingPeriodEnd.plusDays(30));
        waterBill.setTax(BigDecimal.ZERO);
        waterBill.setStatus(WaterBillStatus.PENDING);
        waterBill.setDeliveryMethod(DeliveryMethod.WHATSAPP);
        waterBill.setCustomerAddress(customerAddress);
        waterBill.setTownAddress(customerAddress.getTownAddress());
        
        waterBill.calculateFields();
        return createWaterBill(waterBill);
    }

    @Override
    public WaterBill getWaterBillById(Long id) {
        return waterBillRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Water bill not found with id: " + id));
    }

    @Override
    public WaterBill getWaterBillByBillNumber(String billNumber) {
        return waterBillRepository.findByBillNumber(billNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Water bill not found with bill number: " + billNumber));
    }

    @Override
    public List<WaterBill> getAllWaterBills() {
        return waterBillRepository.findAll();
    }

    @Override
    public WaterBill updateWaterBill(Long id, WaterBill waterBill) {
        WaterBill existingBill = getWaterBillById(id);
        
        // Update fields
        existingBill.setCustomerName(waterBill.getCustomerName());
        existingBill.setCustomerPhone(waterBill.getCustomerPhone());
        existingBill.setWhatsappNumber(waterBill.getWhatsappNumber());
        existingBill.setHomeAddress(waterBill.getHomeAddress());
        existingBill.setMeterNumber(waterBill.getMeterNumber());
        existingBill.setBillingPeriodStart(waterBill.getBillingPeriodStart());
        existingBill.setBillingPeriodEnd(waterBill.getBillingPeriodEnd());
        existingBill.setPreviousReading(waterBill.getPreviousReading());
        existingBill.setCurrentReading(waterBill.getCurrentReading());
        existingBill.setRatePerUnit(waterBill.getRatePerUnit());
        existingBill.setTax(waterBill.getTax());
        existingBill.setDueDate(waterBill.getDueDate());
        existingBill.setStatus(waterBill.getStatus());
        existingBill.setDeliveryMethod(waterBill.getDeliveryMethod());
        existingBill.setNotes(waterBill.getNotes());
        existingBill.setCustomerAddress(waterBill.getCustomerAddress());
        existingBill.setTownAddress(waterBill.getTownAddress());

        existingBill.calculateFields();
        return waterBillRepository.save(existingBill);
    }

    @Override
    public void deleteWaterBill(Long id) {
        if (!waterBillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Water bill not found with id: " + id);
        }
        waterBillRepository.deleteById(id);
    }

    // Bill delivery methods
    @Override
    public boolean sendWaterBillToWhatsApp(Long billId) {
        return sendWaterBill(billId, DeliveryMethod.WHATSAPP);
    }

    @Override
    public boolean sendWaterBillToWhatsApp(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return sendWaterBillToWhatsApp(waterBill.getId());
    }

    @Override
    public boolean sendWaterBillToAddress(Long billId) {
        return sendWaterBill(billId, DeliveryMethod.POSTAL_MAIL);
    }

    @Override
    public boolean sendWaterBillToAddress(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return sendWaterBillToAddress(waterBill.getId());
    }

    @Override
    public boolean sendWaterBill(Long billId, DeliveryMethod deliveryMethod) {
        WaterBill waterBill = getWaterBillById(billId);
        
        try {
            String message = "";
            boolean success = false;
            
            switch (deliveryMethod) {
                case WHATSAPP:
                    message = formatBillForWhatsApp(waterBill);
                    success = sendWhatsAppMessage(waterBill.getWhatsAppNumberForDelivery(), message, waterBill);
                    break;
                case EMAIL:
                    message = formatBillForEmail(waterBill);
                    success = sendEmailMessage(waterBill.getCustomerPhoneForDelivery(), message);
                    break;
                case SMS:
                    message = formatBillForSMS(waterBill);
                    success = sendSMSMessage(waterBill.getCustomerPhoneForDelivery(), message);
                    break;
                case POSTAL_MAIL:
                    message = formatBillForAddress(waterBill);
                    success = sendPostalMail(waterBill.getFullAddressForDelivery(), message);
                    break;
                case HAND_DELIVERY:
                    message = formatBillForAddress(waterBill);
                    success = sendHandDelivery(waterBill.getFullAddressForDelivery(), message);
                    break;
            }
            
            // Update delivery status
            waterBill.setSentAt(LocalDateTime.now());
            waterBill.setDeliveryStatus(success ? "SENT" : "FAILED");
            waterBill.setDeliveryMessage(message);
            waterBill.setStatus(WaterBillStatus.SENT);
            waterBillRepository.save(waterBill);
            
            log.info("Water bill {} sent via {}: {}", billId, deliveryMethod, success ? "SUCCESS" : "FAILED");
            return success;
            
        } catch (Exception e) {
            log.error("Error sending water bill {} via {}: {}", billId, deliveryMethod, e.getMessage());
            waterBill.setDeliveryStatus("FAILED");
            waterBill.setDeliveryMessage("Error: " + e.getMessage());
            waterBillRepository.save(waterBill);
            return false;
        }
    }

    @Override
    public boolean sendWaterBill(String billNumber, DeliveryMethod deliveryMethod) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return sendWaterBill(waterBill.getId(), deliveryMethod);
    }

    @Override
    public boolean sendBulkWaterBills(List<Long> billIds, DeliveryMethod deliveryMethod) {
        boolean allSuccess = true;
        for (Long billId : billIds) {
            boolean success = sendWaterBill(billId, deliveryMethod);
            if (!success) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    @Override
    public boolean sendBulkWaterBillsByStatus(WaterBillStatus status, DeliveryMethod deliveryMethod) {
        List<WaterBill> bills = getWaterBillsByStatus(status);
        List<Long> billIds = bills.stream().map(WaterBill::getId).toList();
        return sendBulkWaterBills(billIds, deliveryMethod);
    }

    // Search and filter methods
    @Override
    public List<WaterBill> getWaterBillsByCustomerPhone(String customerPhone) {
        return waterBillRepository.findByCustomerPhone(customerPhone);
    }

    @Override
    public List<WaterBill> getWaterBillsByWhatsAppNumber(String whatsappNumber) {
        return waterBillRepository.findByWhatsappNumber(whatsappNumber);
    }

    @Override
    public List<WaterBill> getWaterBillsByMeterNumber(String meterNumber) {
        return waterBillRepository.findByMeterNumber(meterNumber);
    }

    @Override
    public List<WaterBill> getWaterBillsByStatus(WaterBillStatus status) {
        return waterBillRepository.findByStatus(status);
    }

    @Override
    public List<WaterBill> getWaterBillsByDeliveryMethod(DeliveryMethod deliveryMethod) {
        return waterBillRepository.findByDeliveryMethod(deliveryMethod);
    }

    @Override
    public List<WaterBill> getWaterBillsByCustomerName(String customerName) {
        return waterBillRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    @Override
    public List<WaterBill> getWaterBillsByHomeAddress(String address) {
        return waterBillRepository.findByHomeAddressContainingIgnoreCase(address);
    }

    @Override
    public List<WaterBill> getOverdueWaterBills() {
        return waterBillRepository.findOverdueBills(LocalDate.now());
    }

    @Override
    public List<WaterBill> getWaterBillsByBillingPeriod(LocalDate startDate, LocalDate endDate) {
        return waterBillRepository.findByBillingPeriod(startDate, endDate);
    }

    @Override
    public List<WaterBill> getWaterBillsDueOnDate(LocalDate date) {
        return waterBillRepository.findBillsDueOnDate(date);
    }

    @Override
    public List<WaterBill> getWaterBillsDueBetweenDates(LocalDate startDate, LocalDate endDate) {
        return waterBillRepository.findBillsDueBetweenDates(startDate, endDate);
    }

    @Override
    public List<WaterBill> getWaterBillsWithHighConsumption(BigDecimal threshold) {
        return waterBillRepository.findByHighConsumption(threshold);
    }

    @Override
    public List<WaterBill> getWaterBillsWithHighAmount(BigDecimal amount) {
        return waterBillRepository.findByHighAmount(amount);
    }

    // Business logic methods
    @Override
    public WaterBill generateWaterBill(String customerName, String customerPhone, String whatsappNumber, 
                                     String homeAddress, String meterNumber, BigDecimal previousReading, 
                                     BigDecimal currentReading, BigDecimal ratePerUnit, LocalDate billingPeriodStart, 
                                     LocalDate billingPeriodEnd) {
        
        WaterBill waterBill = new WaterBill();
        waterBill.setBillNumber(generateBillNumber());
        waterBill.setCustomerName(customerName);
        waterBill.setCustomerPhone(customerPhone);
        waterBill.setWhatsappNumber(whatsappNumber);
        waterBill.setHomeAddress(homeAddress);
        waterBill.setMeterNumber(meterNumber);
        waterBill.setPreviousReading(previousReading);
        waterBill.setCurrentReading(currentReading);
        waterBill.setRatePerUnit(ratePerUnit);
        waterBill.setBillingPeriodStart(billingPeriodStart);
        waterBill.setBillingPeriodEnd(billingPeriodEnd);
        waterBill.setDueDate(billingPeriodEnd.plusDays(30));
        waterBill.setTax(BigDecimal.ZERO);
        waterBill.setStatus(WaterBillStatus.PENDING);
        waterBill.setDeliveryMethod(DeliveryMethod.WHATSAPP);
        
        waterBill.calculateFields();
        return createWaterBill(waterBill);
    }

    @Override
    public WaterBill calculateBillAmounts(Long billId) {
        WaterBill waterBill = getWaterBillById(billId);
        waterBill.calculateFields();
        return waterBillRepository.save(waterBill);
    }

    @Override
    public WaterBill calculateBillAmounts(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return calculateBillAmounts(waterBill.getId());
    }

    @Override
    public WaterBill markAsPaid(Long billId) {
        WaterBill waterBill = getWaterBillById(billId);
        waterBill.setStatus(WaterBillStatus.PAID);
        return waterBillRepository.save(waterBill);
    }

    @Override
    public WaterBill markAsPaid(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return markAsPaid(waterBill.getId());
    }

    @Override
    public WaterBill markAsOverdue(Long billId) {
        WaterBill waterBill = getWaterBillById(billId);
        waterBill.setStatus(WaterBillStatus.OVERDUE);
        return waterBillRepository.save(waterBill);
    }

    @Override
    public WaterBill markAsOverdue(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return markAsOverdue(waterBill.getId());
    }

    @Override
    public WaterBill updateDeliveryStatus(Long billId, String status, String message) {
        WaterBill waterBill = getWaterBillById(billId);
        waterBill.setDeliveryStatus(status);
        waterBill.setDeliveryMessage(message);
        return waterBillRepository.save(waterBill);
    }

    @Override
    public WaterBill updateDeliveryStatus(String billNumber, String status, String message) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return updateDeliveryStatus(waterBill.getId(), status, message);
    }

    // Reporting methods
    @Override
    public long countWaterBillsByStatus(WaterBillStatus status) {
        return waterBillRepository.countByStatus(status);
    }

    @Override
    public long countWaterBillsByDeliveryMethod(DeliveryMethod deliveryMethod) {
        return waterBillRepository.countByDeliveryMethod(deliveryMethod);
    }

    @Override
    public long countOverdueWaterBills() {
        return waterBillRepository.countOverdueBills(LocalDate.now());
    }

    @Override
    public BigDecimal sumTotalAmountByStatus(WaterBillStatus status) {
        BigDecimal sum = waterBillRepository.sumTotalAmountByStatus(status);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal sumTotalAmountByDeliveryMethod(DeliveryMethod deliveryMethod) {
        BigDecimal sum = waterBillRepository.sumTotalAmountByDeliveryMethod(deliveryMethod);
        return sum != null ? sum : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalOutstandingAmount() {
        return sumTotalAmountByStatus(WaterBillStatus.PENDING)
                .add(sumTotalAmountByStatus(WaterBillStatus.SENT))
                .add(sumTotalAmountByStatus(WaterBillStatus.OVERDUE));
    }

    @Override
    public BigDecimal getTotalPaidAmount() {
        return sumTotalAmountByStatus(WaterBillStatus.PAID);
    }

    @Override
    public BigDecimal getAverageBillAmount() {
        List<WaterBill> allBills = getAllWaterBills();
        if (allBills.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = allBills.stream()
                .map(WaterBill::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.divide(BigDecimal.valueOf(allBills.size()), 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotalConsumption() {
        List<WaterBill> allBills = getAllWaterBills();
        return allBills.stream()
                .map(WaterBill::getConsumption)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Validation methods
    @Override
    public boolean isBillNumberExists(String billNumber) {
        return waterBillRepository.existsByBillNumber(billNumber);
    }

    @Override
    public boolean isMeterNumberExists(String meterNumber) {
        return waterBillRepository.existsByMeterNumber(meterNumber);
    }

    @Override
    public boolean isValidWhatsAppNumber(String whatsappNumber) {
        return WHATSAPP_PATTERN.matcher(whatsappNumber).matches();
    }

    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    @Override
    public boolean isBillOverdue(Long billId) {
        WaterBill waterBill = getWaterBillById(billId);
        return waterBill.getDueDate().isBefore(LocalDate.now()) && 
               waterBill.getStatus() != WaterBillStatus.PAID;
    }

    @Override
    public boolean isBillOverdue(String billNumber) {
        WaterBill waterBill = getWaterBillByBillNumber(billNumber);
        return isBillOverdue(waterBill.getId());
    }

    // Utility methods
    @Override
    public String generateBillNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "WB" + timestamp + random;
    }

    @Override
    public String formatBillForWhatsApp(WaterBill waterBill) {
        return String.format("""
            💧 *WATER BILL NOTIFICATION*
            
            *Bill Number:* %s
            *Customer:* %s
            *Meter Number:* %s
            *Address:* %s
            
            *Billing Period:* %s to %s
            *Previous Reading:* %s
            *Current Reading:* %s
            *Consumption:* %s units
            
            *Rate per Unit:* $%s
            *Subtotal:* $%s
            *Tax:* $%s
            *Total Amount:* $%s
            
            *Due Date:* %s
            
            Please pay your water bill on time to avoid late fees.
            For payment options, contact our customer service.
            
            Thank you for choosing our water service!
            """,
            waterBill.getBillNumber(),
            waterBill.getCustomerName(),
            waterBill.getMeterNumber(),
            waterBill.getFullAddressForDelivery(),
            waterBill.getBillingPeriodStart().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
            waterBill.getBillingPeriodEnd().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
            waterBill.getPreviousReading(),
            waterBill.getCurrentReading(),
            waterBill.getConsumption(),
            waterBill.getRatePerUnit(),
            waterBill.getSubtotal(),
            waterBill.getTax(),
            waterBill.getTotalAmount(),
            waterBill.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        );
    }

    @Override
    public String formatBillForAddress(WaterBill waterBill) {
        return String.format("""
            WATER BILL
            
            Bill Number: %s
            Customer: %s
            Meter Number: %s
            Address: %s
            
            Billing Period: %s to %s
            Previous Reading: %s
            Current Reading: %s
            Consumption: %s units
            
            Rate per Unit: $%s
            Subtotal: $%s
            Tax: $%s
            Total Amount: $%s
            
            Due Date: %s
            
            Please pay your water bill on time to avoid late fees.
            """,
            waterBill.getBillNumber(),
            waterBill.getCustomerName(),
            waterBill.getMeterNumber(),
            waterBill.getFullAddressForDelivery(),
            waterBill.getBillingPeriodStart().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
            waterBill.getBillingPeriodEnd().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
            waterBill.getPreviousReading(),
            waterBill.getCurrentReading(),
            waterBill.getConsumption(),
            waterBill.getRatePerUnit(),
            waterBill.getSubtotal(),
            waterBill.getTax(),
            waterBill.getTotalAmount(),
            waterBill.getDueDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
        );
    }

    @Override
    public String formatBillForEmail(WaterBill waterBill) {
        return formatBillForAddress(waterBill); // Same format for email
    }

    @Override
    public String formatBillForSMS(WaterBill waterBill) {
        return String.format("Water Bill %s: $%s due %s. Call for payment options.",
            waterBill.getBillNumber(),
            waterBill.getTotalAmount(),
            waterBill.getDueDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        );
    }

    // Private helper methods for actual delivery (these would integrate with real services)
    private boolean sendWhatsAppMessage(String whatsappNumber, String message, WaterBill waterBill) {
        // TODO: Integrate with WhatsApp Business API or similar service
        log.info("Sending WhatsApp message to {} for bill {}: {}", whatsappNumber, waterBill.getBillNumber(), message);
        return true; // Simulated success
    }

    private boolean sendEmailMessage(String email, String message) {
        // TODO: Integrate with email service
        log.info("Sending email to {}: {}", email, message);
        return true; // Simulated success
    }

    private boolean sendSMSMessage(String phoneNumber, String message) {
        // TODO: Integrate with SMS service
        log.info("Sending SMS to {}: {}", phoneNumber, message);
        return true; // Simulated success
    }

    private boolean sendPostalMail(String address, String message) {
        // TODO: Integrate with postal service
        log.info("Sending postal mail to {}: {}", address, message);
        return true; // Simulated success
    }

    private boolean sendHandDelivery(String address, String message) {
        // TODO: Integrate with delivery service
        log.info("Sending hand delivery to {}: {}", address, message);
        return true; // Simulated success
    }
} 