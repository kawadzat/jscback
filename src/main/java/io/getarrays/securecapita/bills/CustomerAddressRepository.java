package io.getarrays.securecapita.bills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {

    // Find by customer name
    List<CustomerAddress> findByCustomerName(String customerName);

    // Find by customer name containing (partial match)
    List<CustomerAddress> findByCustomerNameContainingIgnoreCase(String customerName);

    // Find by customer phone
    List<CustomerAddress> findByCustomerPhone(String customerPhone);

    // Find by customer WhatsApp
    List<CustomerAddress> findByCustomerWhatsapp(String customerWhatsapp);

    // Find by customer email
    List<CustomerAddress> findByCustomerEmail(String customerEmail);

    // Find by address type
    List<CustomerAddress> findByAddressType(String addressType);

    // Find primary addresses
    List<CustomerAddress> findByIsPrimaryTrue();

    // Find active addresses
    List<CustomerAddress> findByIsActiveTrue();

    // Find inactive addresses
    List<CustomerAddress> findByIsActiveFalse();

    // Find by neighborhood
    List<CustomerAddress> findByNeighborhood(String neighborhood);

    // Find by landmark
    List<CustomerAddress> findByLandmarkContainingIgnoreCase(String landmark);

    // Find by street address containing
    List<CustomerAddress> findByStreetAddressContainingIgnoreCase(String streetAddress);

    // Find by apartment unit
    List<CustomerAddress> findByApartmentUnit(String apartmentUnit);

    // Find by town address
    List<CustomerAddress> findByTownAddressId(Long townAddressId);

    // Find by town address and customer name
    List<CustomerAddress> findByTownAddressIdAndCustomerNameContainingIgnoreCase(Long townAddressId, String customerName);

    // Find by town address and address type
    List<CustomerAddress> findByTownAddressIdAndAddressType(Long townAddressId, String addressType);

    // Find by town address and active status
    List<CustomerAddress> findByTownAddressIdAndIsActiveTrue(Long townAddressId);

    // Find by customer phone and address type
    List<CustomerAddress> findByCustomerPhoneAndAddressType(String customerPhone, String addressType);

    // Find by customer WhatsApp and address type
    List<CustomerAddress> findByCustomerWhatsappAndAddressType(String customerWhatsapp, String addressType);

    // Find by customer email and address type
    List<CustomerAddress> findByCustomerEmailAndAddressType(String customerEmail, String addressType);

    // Find primary address by customer phone
    Optional<CustomerAddress> findByCustomerPhoneAndIsPrimaryTrue(String customerPhone);

    // Find primary address by customer WhatsApp
    Optional<CustomerAddress> findByCustomerWhatsappAndIsPrimaryTrue(String customerWhatsapp);

    // Find primary address by customer email
    Optional<CustomerAddress> findByCustomerEmailAndIsPrimaryTrue(String customerEmail);

    // Find by multiple criteria
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.customerName = :customerName AND ca.customerPhone = :customerPhone AND ca.addressType = :addressType")
    List<CustomerAddress> findByCustomerNamePhoneAndType(@Param("customerName") String customerName, @Param("customerPhone") String customerPhone, @Param("addressType") String addressType);

    // Search customer addresses by multiple fields
    @Query("SELECT ca FROM CustomerAddress ca WHERE " +
           "LOWER(ca.customerName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ca.streetAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ca.neighborhood) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ca.landmark) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<CustomerAddress> searchCustomerAddresses(@Param("searchTerm") String searchTerm);

    // Find customers by town
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.townName = :townName")
    List<CustomerAddress> findByTownName(@Param("townName") String townName);

    // Find customers by state
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.state = :state")
    List<CustomerAddress> findByState(@Param("state") String state);

    // Find customers by country
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.country = :country")
    List<CustomerAddress> findByCountry(@Param("country") String country);

    // Find customers by district
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.district = :district")
    List<CustomerAddress> findByDistrict(@Param("district") String district);

    // Find customers by region
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.region = :region")
    List<CustomerAddress> findByRegion(@Param("region") String region);

    // Find customers by postal code
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.postalCode = :postalCode")
    List<CustomerAddress> findByPostalCode(@Param("postalCode") String postalCode);

    // Find customers by town and neighborhood
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.townName = :townName AND ca.neighborhood = :neighborhood")
    List<CustomerAddress> findByTownNameAndNeighborhood(@Param("townName") String townName, @Param("neighborhood") String neighborhood);

    // Find customers by town and address type
    @Query("SELECT ca FROM CustomerAddress ca WHERE ca.townAddress.townName = :townName AND ca.addressType = :addressType")
    List<CustomerAddress> findByTownNameAndAddressType(@Param("townName") String townName, @Param("addressType") String addressType);

    // Count customers by town
    @Query("SELECT COUNT(ca) FROM CustomerAddress ca WHERE ca.townAddress.townName = :townName")
    long countByTownName(@Param("townName") String townName);

    // Count customers by state
    @Query("SELECT COUNT(ca) FROM CustomerAddress ca WHERE ca.townAddress.state = :state")
    long countByState(@Param("state") String state);

    // Count customers by country
    @Query("SELECT COUNT(ca) FROM CustomerAddress ca WHERE ca.townAddress.country = :country")
    long countByCountry(@Param("country") String country);

    // Count customers by address type
    long countByAddressType(String addressType);

    // Count active customers
    long countByIsActiveTrue();

    // Count inactive customers
    long countByIsActiveFalse();

    // Count primary addresses
    long countByIsPrimaryTrue();

    // Check if customer phone exists
    boolean existsByCustomerPhone(String customerPhone);

    // Check if customer WhatsApp exists
    boolean existsByCustomerWhatsapp(String customerWhatsapp);

    // Check if customer email exists
    boolean existsByCustomerEmail(String customerEmail);

    // Check if customer has primary address
    boolean existsByCustomerPhoneAndIsPrimaryTrue(String customerPhone);

    // Get all unique neighborhoods
    @Query("SELECT DISTINCT ca.neighborhood FROM CustomerAddress ca ORDER BY ca.neighborhood")
    List<String> findAllNeighborhoods();

    // Get all unique address types
    @Query("SELECT DISTINCT ca.addressType FROM CustomerAddress ca ORDER BY ca.addressType")
    List<String> findAllAddressTypes();

    // Get all unique landmarks
    @Query("SELECT DISTINCT ca.landmark FROM CustomerAddress ca WHERE ca.landmark IS NOT NULL ORDER BY ca.landmark")
    List<String> findAllLandmarks();

    // Get all unique apartment units
    @Query("SELECT DISTINCT ca.apartmentUnit FROM CustomerAddress ca ORDER BY ca.apartmentUnit")
    List<String> findAllApartmentUnits();
} 