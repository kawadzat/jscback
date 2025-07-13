package io.getarrays.securecapita.bills;

import java.util.List;
import java.util.Map;

public interface TownAddressService {

    // Basic CRUD operations
    TownAddress createTownAddress(TownAddress townAddress);
    TownAddress getTownAddressById(Long id);
    TownAddress getTownAddressByTownCode(String townCode);
    TownAddress getTownAddressByTownName(String townName);
    List<TownAddress> getAllTownAddresses();
    TownAddress updateTownAddress(Long id, TownAddress townAddress);
    void deleteTownAddress(Long id);

    // Search and filter methods
    List<TownAddress> getTownAddressesByState(String state);
    List<TownAddress> getTownAddressesByCountry(String country);
    List<TownAddress> getTownAddressesByDistrict(String district);
    List<TownAddress> getTownAddressesByRegion(String region);
    List<TownAddress> getTownAddressesByPostalCode(String postalCode);
    List<TownAddress> getTownAddressesByTimezone(String timezone);
    List<TownAddress> getTownAddressesByCurrency(String currency);
    List<TownAddress> getTownAddressesByLanguage(String language);
    List<TownAddress> getActiveTownAddresses();
    List<TownAddress> getInactiveTownAddresses();
    List<TownAddress> searchTownAddresses(String searchTerm);

    // Contact information methods
    List<TownAddress> getTownAddressesByPhoneNumber(String phoneNumber);
    List<TownAddress> getTownAddressesByWhatsAppNumber(String whatsappNumber);
    List<TownAddress> getTownAddressesByEmailDomain(String emailDomain);
    List<TownAddress> getTownAddressesByPostalCodePattern(String postalCodePattern);
    TownAddress getTownAddressByContactInfo(String phone, String whatsapp);

    // Business logic methods
    TownAddress activateTownAddress(Long id);
    TownAddress deactivateTownAddress(Long id);
    TownAddress updateContactInfo(Long id, String phone, String whatsapp, String email);
    TownAddress updateBillingContactInfo(Long id, String billingPhone, String billingWhatsapp);
    TownAddress updateEmergencyContactInfo(Long id, String emergencyPhone);

    // Reporting methods
    long countTownAddressesByState(String state);
    long countTownAddressesByCountry(String country);
    long countActiveTownAddresses();
    long countInactiveTownAddresses();
    long countTownAddressesByCurrency(String currency);
    long countTownAddressesByLanguage(String language);
    Map<String, Long> getTownAddressesCountByState();
    Map<String, Long> getTownAddressesCountByCountry();
    Map<String, Long> getTownAddressesCountByCurrency();
    Map<String, Long> getTownAddressesCountByLanguage();

    // Validation methods
    boolean isTownCodeExists(String townCode);
    boolean isTownNameExists(String townName);
    boolean isPostalCodeExists(String postalCode);
    boolean isValidPhoneNumber(String phoneNumber);
    boolean isValidWhatsAppNumber(String whatsappNumber);
    boolean isValidEmail(String email);
    boolean isValidPostalCode(String postalCode);

    // Utility methods
    List<String> getAllStates();
    List<String> getAllCountries();
    List<String> getAllDistricts();
    List<String> getAllRegions();
    List<String> getAllCurrencies();
    List<String> getAllLanguages();
    List<String> getAllTimezones();
    String generateTownCode(String townName, String state);
    Map<String, Object> getTownAddressSummary(Long id);
    Map<String, Object> getTownAddressContactInfo(Long id);
    Map<String, Object> getTownAddressBillingInfo(Long id);
} 