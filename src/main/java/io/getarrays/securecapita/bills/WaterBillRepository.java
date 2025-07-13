package io.getarrays.securecapita.bills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaterBillRepository extends JpaRepository<WaterBill, Long> {

    // Find by bill number
    Optional<WaterBill> findByBillNumber(String billNumber);

    // Find by customer phone
    List<WaterBill> findByCustomerPhone(String customerPhone);

    // Find by WhatsApp number
    List<WaterBill> findByWhatsappNumber(String whatsappNumber);

    // Find by meter number
    List<WaterBill> findByMeterNumber(String meterNumber);

    // Find by status
    List<WaterBill> findByStatus(WaterBillStatus status);

    // Find by delivery method
    List<WaterBill> findByDeliveryMethod(DeliveryMethod deliveryMethod);

    // Find pending bills
    List<WaterBill> findByStatusAndDeliveryMethod(WaterBillStatus status, DeliveryMethod deliveryMethod);

    // Find by customer address
    List<WaterBill> findByCustomerAddressId(Long customerAddressId);

    // Find overdue bills
    @Query("SELECT wb FROM WaterBill wb WHERE wb.dueDate < :currentDate AND wb.status != 'PAID'")
    List<WaterBill> findOverdueBills(@Param("currentDate") LocalDate currentDate);

    // Find bills by billing period
    @Query("SELECT wb FROM WaterBill wb WHERE wb.billingPeriodStart >= :startDate AND wb.billingPeriodEnd <= :endDate")
    List<WaterBill> findByBillingPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find bills by customer name (partial match)
    @Query("SELECT wb FROM WaterBill wb WHERE LOWER(wb.customerName) LIKE LOWER(CONCAT('%', :customerName, '%'))")
    List<WaterBill> findByCustomerNameContainingIgnoreCase(@Param("customerName") String customerName);

    // Find bills by home address (partial match)
    @Query("SELECT wb FROM WaterBill wb WHERE LOWER(wb.homeAddress) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<WaterBill> findByHomeAddressContainingIgnoreCase(@Param("address") String address);

    // Find bills with high consumption
    @Query("SELECT wb FROM WaterBill wb WHERE wb.consumption > :threshold")
    List<WaterBill> findByHighConsumption(@Param("threshold") BigDecimal threshold);

    // Find bills with high amounts
    @Query("SELECT wb FROM WaterBill wb WHERE wb.totalAmount > :amount")
    List<WaterBill> findByHighAmount(@Param("amount") BigDecimal amount);

    // Count bills by status
    long countByStatus(WaterBillStatus status);

    // Count bills by delivery method
    long countByDeliveryMethod(DeliveryMethod deliveryMethod);

    // Count overdue bills
    @Query("SELECT COUNT(wb) FROM WaterBill wb WHERE wb.dueDate < :currentDate AND wb.status != 'PAID'")
    long countOverdueBills(@Param("currentDate") LocalDate currentDate);

    // Sum total amount by status
    @Query("SELECT SUM(wb.totalAmount) FROM WaterBill wb WHERE wb.status = :status")
    BigDecimal sumTotalAmountByStatus(@Param("status") WaterBillStatus status);

    // Sum total amount by delivery method
    @Query("SELECT SUM(wb.totalAmount) FROM WaterBill wb WHERE wb.deliveryMethod = :deliveryMethod")
    BigDecimal sumTotalAmountByDeliveryMethod(@Param("deliveryMethod") DeliveryMethod deliveryMethod);

    // Find bills sent today
    @Query("SELECT wb FROM WaterBill wb WHERE DATE(wb.sentAt) = :date")
    List<WaterBill> findBillsSentOnDate(@Param("date") LocalDate date);

    // Find bills due today
    @Query("SELECT wb FROM WaterBill wb WHERE wb.dueDate = :date")
    List<WaterBill> findBillsDueOnDate(@Param("date") LocalDate date);

    // Find bills due in next X days
    @Query("SELECT wb FROM WaterBill wb WHERE wb.dueDate BETWEEN :startDate AND :endDate")
    List<WaterBill> findBillsDueBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Check if bill number exists
    boolean existsByBillNumber(String billNumber);

    // Check if meter number exists
    boolean existsByMeterNumber(String meterNumber);
} 