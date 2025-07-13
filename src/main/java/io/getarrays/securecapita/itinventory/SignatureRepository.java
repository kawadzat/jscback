package io.getarrays.securecapita.itinventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {
    
    /**
     * Find signature by its hash
     */
    Optional<Signature> findBySignatureHash(String signatureHash);
    
    /**
     * Check if signature exists by hash
     */
    boolean existsBySignatureHash(String signatureHash);
    
    /**
     * Find all signatures by user
     */
    List<Signature> findBySignedById(Long userId);
    
    /**
     * Find all valid signatures by user
     */
    List<Signature> findBySignedByIdAndIsValidTrue(Long userId);
    
    /**
     * Find all signatures by laptop ID
     */
    List<Signature> findByLaptopId(Long laptopId);
    
    /**
     * Find all valid signatures by laptop ID
     */
    List<Signature> findByLaptopIdAndIsValidTrue(Long laptopId);
    
    /**
     * Find all signatures by laptop acknowledgment
     */
    List<Signature> findByLaptopAcknowledgmentId(Long acknowledgmentId);
    

    
    /**
     * Find all signatures by purpose
     */
    List<Signature> findBySignaturePurpose(String purpose);
    
    /**
     * Find all signatures by compliance level
     */
    List<Signature> findByComplianceLevel(String complianceLevel);
    
    /**
     * Find all expired signatures
     */
    @Query("SELECT s FROM Signature s WHERE s.expirationTimestamp IS NOT NULL AND s.expirationTimestamp < :now")
    List<Signature> findExpiredSignatures(@Param("now") LocalDateTime now);
    
    /**
     * Find all signatures expiring within specified days
     */
    @Query("SELECT s FROM Signature s WHERE s.expirationTimestamp IS NOT NULL AND s.expirationTimestamp BETWEEN :now AND :futureDate")
    List<Signature> findSignaturesExpiringWithinDays(@Param("now") LocalDateTime now, @Param("futureDate") LocalDateTime futureDate);
    
    /**
     * Find all revoked signatures
     */
    @Query("SELECT s FROM Signature s WHERE s.revocationTimestamp IS NOT NULL")
    List<Signature> findRevokedSignatures();
    
    /**
     * Find all archived signatures
     */
    List<Signature> findByIsArchivedTrue();
    
    /**
     * Find all signatures by validation status
     */
    List<Signature> findByValidationStatus(String validationStatus);
    
    /**
     * Find all signatures created within date range
     */
    @Query("SELECT s FROM Signature s WHERE s.signatureTimestamp BETWEEN :startDate AND :endDate")
    List<Signature> findBySignatureTimestampBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find all signatures last validated within date range
     */
    @Query("SELECT s FROM Signature s WHERE s.lastValidated BETWEEN :startDate AND :endDate")
    List<Signature> findByLastValidatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count signatures by user
     */
    long countBySignedById(Long userId);
    
    /**
     * Count valid signatures by user
     */
    long countBySignedByIdAndIsValidTrue(Long userId);
    
    /**
     * Count signatures by laptop ID
     */
    long countByLaptopId(Long laptopId);
    

    
    /**
     * Count signatures by purpose
     */
    long countBySignaturePurpose(String purpose);
    
    /**
     * Count signatures by compliance level
     */
    long countByComplianceLevel(String complianceLevel);
    
    /**
     * Count expired signatures
     */
    @Query("SELECT COUNT(s) FROM Signature s WHERE s.expirationTimestamp IS NOT NULL AND s.expirationTimestamp < :now")
    long countExpiredSignatures(@Param("now") LocalDateTime now);
    
    /**
     * Count revoked signatures
     */
    @Query("SELECT COUNT(s) FROM Signature s WHERE s.revocationTimestamp IS NOT NULL")
    long countRevokedSignatures();
    
    /**
     * Count archived signatures
     */
    long countByIsArchivedTrue();
    
    /**
     * Find signatures with high validation attempts (potential issues)
     */
    @Query("SELECT s FROM Signature s WHERE s.validationAttempts > :threshold")
    List<Signature> findSignaturesWithHighValidationAttempts(@Param("threshold") Integer threshold);
    
    /**
     * Find signatures by algorithm
     */
    List<Signature> findBySignatureAlgorithm(String algorithm);
    
    /**
     * Find signatures by key size
     */
    List<Signature> findByKeySize(Integer keySize);
    
    /**
     * Find signatures by version
     */
    List<Signature> findBySignatureVersion(String version);
    
    /**
     * Find all currently valid signatures (not expired, not revoked, not archived)
     */
    @Query("SELECT s FROM Signature s WHERE s.isValid = true AND s.isArchived = false AND (s.expirationTimestamp IS NULL OR s.expirationTimestamp > :now) AND s.revocationTimestamp IS NULL")
    List<Signature> findCurrentlyValidSignatures(@Param("now") LocalDateTime now);
    
    /**
     * Find signatures that need validation (not validated recently)
     */
    @Query("SELECT s FROM Signature s WHERE s.lastValidated IS NULL OR s.lastValidated < :threshold")
    List<Signature> findSignaturesNeedingValidation(@Param("threshold") LocalDateTime threshold);
    
    /**
     * Find signatures by IP address
     */
    List<Signature> findByIpAddress(String ipAddress);
    
    /**
     * Find signatures by user agent pattern
     */
    @Query("SELECT s FROM Signature s WHERE s.userAgent LIKE %:userAgentPattern%")
    List<Signature> findByUserAgentContaining(@Param("userAgentPattern") String userAgentPattern);
} 