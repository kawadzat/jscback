package io.getarrays.securecapita.bills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TownAddressRepository extends JpaRepository<TownAddress, Long> {

    // Find by town code
    Optional<TownAddress> findByTownCode(String townCode);

    // Find by town name
    Optional<TownAddress> findByTownName(String townName);

    // Find by town name containing (partial match)
    List<TownAddress> findByTownNameContainingIgnoreCase(String townName);

    // Find by state
    List<TownAddress> findByState(String state);

    // Find by country
    List<TownAddress> findByCountry(String country);

    // Find by district
    List<TownAddress> findByDistrict(String district);

    // Find by region
    List<TownAddress> findByRegion(String region);

    // Find by postal code
    List<TownAddress> findByPostalCode(String postalCode);

    // Find by timezone
    List<TownAddress> findByTimezone(String timezone);

    // Find by currency
    List<TownAddress> findByCurrency(String currency);

    // Find by language
    List<TownAddress> findByLanguage(String language);

    // Find active towns
    List<TownAddress> findByIsActiveTrue();

    // Find inactive towns
    List<TownAddress> findByIsActiveFalse();

    // Find by state and country
    List<TownAddress> findByStateAndCountry(String state, String country);

    // Find by district and state
    List<TownAddress> findByDistrictAndState(String district, String state);

    // Find by region and country
    List<TownAddress> findByRegionAndCountry(String region, String country);

    // Find towns by phone number (any office phone)
    @Query("SELECT ta FROM TownAddress ta WHERE ta.mainOfficePhone = :phone OR ta.billingOfficePhone = :phone OR ta.customerServicePhone = :phone OR ta.emergencyPhone = :phone")
    List<TownAddress> findByAnyPhoneNumber(@Param("phone") String phone);

    // Find towns by WhatsApp number (any office WhatsApp)
    @Query("SELECT ta FROM TownAddress ta WHERE ta.mainOfficeWhatsapp = :whatsapp OR ta.billingOfficeWhatsapp = :whatsapp OR ta.customerServiceWhatsapp = :whatsapp")
    List<TownAddress> findByAnyWhatsAppNumber(@Param("whatsapp") String whatsapp);

    // Find towns by email domain
    @Query("SELECT ta FROM TownAddress ta WHERE ta.email LIKE %:domain")
    List<TownAddress> findByEmailDomain(@Param("domain") String domain);

    // Find towns by postal code pattern
    @Query("SELECT ta FROM TownAddress ta WHERE ta.postalCode LIKE %:postalCodePattern%")
    List<TownAddress> findByPostalCodePattern(@Param("postalCodePattern") String postalCodePattern);

    // Find towns with specific contact information
    @Query("SELECT ta FROM TownAddress ta WHERE ta.mainOfficePhone = :phone AND ta.mainOfficeWhatsapp = :whatsapp")
    List<TownAddress> findByPhoneAndWhatsApp(@Param("phone") String phone, @Param("whatsapp") String whatsapp);

    // Find towns by multiple criteria
    @Query("SELECT ta FROM TownAddress ta WHERE ta.state = :state AND ta.country = :country AND ta.isActive = :isActive")
    List<TownAddress> findByStateCountryAndActive(@Param("state") String state, @Param("country") String country, @Param("isActive") Boolean isActive);

    // Search towns by multiple fields
    @Query("SELECT ta FROM TownAddress ta WHERE " +
           "LOWER(ta.townName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ta.state) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ta.district) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ta.region) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ta.country) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<TownAddress> searchTowns(@Param("searchTerm") String searchTerm);

    // Count towns by state
    long countByState(String state);

    // Count towns by country
    long countByCountry(String country);

    // Count active towns
    long countByIsActiveTrue();

    // Count inactive towns
    long countByIsActiveFalse();

    // Count towns by currency
    long countByCurrency(String currency);

    // Count towns by language
    long countByLanguage(String language);

    // Check if town code exists
    boolean existsByTownCode(String townCode);

    // Check if town name exists
    boolean existsByTownName(String townName);

    // Check if postal code exists
    boolean existsByPostalCode(String postalCode);

    // Get all unique states
    @Query("SELECT DISTINCT ta.state FROM TownAddress ta ORDER BY ta.state")
    List<String> findAllStates();

    // Get all unique countries
    @Query("SELECT DISTINCT ta.country FROM TownAddress ta ORDER BY ta.country")
    List<String> findAllCountries();

    // Get all unique districts
    @Query("SELECT DISTINCT ta.district FROM TownAddress ta ORDER BY ta.district")
    List<String> findAllDistricts();

    // Get all unique regions
    @Query("SELECT DISTINCT ta.region FROM TownAddress ta ORDER BY ta.region")
    List<String> findAllRegions();

    // Get all unique currencies
    @Query("SELECT DISTINCT ta.currency FROM TownAddress ta ORDER BY ta.currency")
    List<String> findAllCurrencies();

    // Get all unique languages
    @Query("SELECT DISTINCT ta.language FROM TownAddress ta ORDER BY ta.language")
    List<String> findAllLanguages();

    // Get all unique timezones
    @Query("SELECT DISTINCT ta.timezone FROM TownAddress ta ORDER BY ta.timezone")
    List<String> findAllTimezones();
} 