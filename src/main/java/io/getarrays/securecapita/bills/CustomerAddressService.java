package io.getarrays.securecapita.bills;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerAddressService {

    // Basic CRUD operations
    CustomerAddress createCustomerAddress(CustomerAddress customerAddress);
    CustomerAddress getCustomerAddressById(Long id);
    List<CustomerAddress> getAllCustomerAddresses();
    CustomerAddress updateCustomerAddress(Long id, CustomerAddress customerAddress);
    void deleteCustomerAddress(Long id);

    // Search and filter methods
    List<CustomerAddress> getCustomerAddressesByCustomerName(String customerName);
    List<CustomerAddress> getCustomerAddressesByCustomerPhone(String customerPhone);
    List<CustomerAddress> getCustomerAddressesByCustomerWhatsapp(String customerWhatsapp);
    List<CustomerAddress> getCustomerAddressesByCustomerEmail(String customerEmail);
    List<CustomerAddress> getCustomerAddressesByAddressType(String addressType);
    List<CustomerAddress> getPrimaryCustomerAddresses();
    List<CustomerAddress> getActiveCustomerAddresses();
    List<CustomerAddress> getInactiveCustomerAddresses();
    List<CustomerAddress> getCustomerAddressesByNeighborhood(String neighborhood);
    List<CustomerAddress> getCustomerAddressesByLandmark(String landmark);
    List<CustomerAddress> getCustomerAddressesByStreetAddress(String streetAddress);
    List<CustomerAddress> getCustomerAddressesByApartmentUnit(String apartmentUnit);
    List<CustomerAddress> searchCustomerAddresses(String searchTerm);

    // Town-based methods
    List<CustomerAddress> getCustomerAddressesByTownAddressId(Long townAddressId);
    List<CustomerAddress> getCustomerAddressesByTownName(String townName);
    List<CustomerAddress> getCustomerAddressesByState(String state);
    List<CustomerAddress> getCustomerAddressesByCountry(String country);
    List<CustomerAddress> getCustomerAddressesByDistrict(String district);
    List<CustomerAddress> getCustomerAddressesByRegion(String region);
    List<CustomerAddress> getCustomerAddressesByPostalCode(String postalCode);
    List<CustomerAddress> getCustomerAddressesByTownAndNeighborhood(String townName, String neighborhood);
    List<CustomerAddress> getCustomerAddressesByTownAndAddressType(String townName, String addressType);

    // Primary address methods
    Optional<CustomerAddress> getPrimaryAddressByCustomerPhone(String customerPhone);
    Optional<CustomerAddress> getPrimaryAddressByCustomerWhatsapp(String customerWhatsapp);
    Optional<CustomerAddress> getPrimaryAddressByCustomerEmail(String customerEmail);
    CustomerAddress setPrimaryAddress(Long customerAddressId);
    CustomerAddress removePrimaryAddress(Long customerAddressId);

    // Business logic methods
    CustomerAddress activateCustomerAddress(Long id);
    CustomerAddress deactivateCustomerAddress(Long id);
    CustomerAddress updateContactInfo(Long id, String phone, String whatsapp, String email);
    CustomerAddress updateAddressInfo(Long id, String streetAddress, String apartmentUnit, String neighborhood, String landmark);
    CustomerAddress changeAddressType(Long id, String addressType);

    // Multiple criteria methods
    List<CustomerAddress> getCustomerAddressesByCustomerNamePhoneAndType(String customerName, String customerPhone, String addressType);
    List<CustomerAddress> getCustomerAddressesByPhoneAndAddressType(String customerPhone, String addressType);
    List<CustomerAddress> getCustomerAddressesByWhatsappAndAddressType(String customerWhatsapp, String addressType);
    List<CustomerAddress> getCustomerAddressesByEmailAndAddressType(String customerEmail, String addressType);

    // Reporting methods
    long countCustomerAddressesByTownName(String townName);
    long countCustomerAddressesByState(String state);
    long countCustomerAddressesByCountry(String country);
    long countCustomerAddressesByAddressType(String addressType);
    long countActiveCustomerAddresses();
    long countInactiveCustomerAddresses();
    long countPrimaryAddresses();
    Map<String, Long> getCustomerAddressesCountByTown();
    Map<String, Long> getCustomerAddressesCountByState();
    Map<String, Long> getCustomerAddressesCountByCountry();
    Map<String, Long> getCustomerAddressesCountByAddressType();
    Map<String, Long> getCustomerAddressesCountByNeighborhood();

    // Validation methods
    boolean isCustomerPhoneExists(String customerPhone);
    boolean isCustomerWhatsappExists(String customerWhatsapp);
    boolean isCustomerEmailExists(String customerEmail);
    boolean isCustomerHasPrimaryAddress(String customerPhone);
    boolean isValidPhoneNumber(String phoneNumber);
    boolean isValidWhatsAppNumber(String whatsappNumber);
    boolean isValidEmail(String email);

    // Utility methods
    List<String> getAllNeighborhoods();
    List<String> getAllAddressTypes();
    List<String> getAllLandmarks();
    List<String> getAllApartmentUnits();
    Map<String, Object> getCustomerAddressSummary(Long id);
    Map<String, Object> getCustomerAddressContactInfo(Long id);
    Map<String, Object> getCustomerAddressFullInfo(Long id);
    String generateCustomerAddressCode(String customerName, String townName);
} 