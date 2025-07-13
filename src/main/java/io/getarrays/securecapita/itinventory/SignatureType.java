package io.getarrays.securecapita.itinventory;

/**
 * Enum representing different types of signatures that can be used in the system.
 * Each type has different security levels and use cases.
 */
public enum SignatureType {
    
    /**
     * Digital signature created using cryptographic algorithms
     * Highest security level, typically used for legal documents
     */
    DIGITAL_SIGNATURE("Digital Signature", "Cryptographic digital signature with certificate"),
    
    /**
     * Hand-drawn signature captured via touch screen or mouse
     * Medium security level, commonly used for acknowledgments
     */
    DRAWN_SIGNATURE("Drawn Signature", "Hand-drawn signature captured electronically"),
    
    /**
     * Typed signature using keyboard input
     * Basic security level, used for simple acknowledgments
     */
    TYPED_SIGNATURE("Typed Signature", "Typed signature using keyboard input"),
    
    /**
     * Electronic signature using biometric data (fingerprint, face recognition)
     * High security level, used for sensitive operations
     */
    BIOMETRIC_SIGNATURE("Biometric Signature", "Electronic signature using biometric data"),
    
    /**
     * Click-to-sign signature (simple checkbox or button click)
     * Basic security level, used for terms acceptance
     */
    CLICK_SIGNATURE("Click Signature", "Simple click-to-sign acknowledgment"),
    
    /**
     * Voice signature using audio recording
     * Medium security level, used for verbal acknowledgments
     */
    VOICE_SIGNATURE("Voice Signature", "Voice signature using audio recording"),
    
    /**
     * Image-based signature (uploaded signature image)
     * Medium security level, used for scanned signatures
     */
    IMAGE_SIGNATURE("Image Signature", "Uploaded signature image"),
    
    /**
     * Multi-factor signature requiring multiple authentication methods
     * Highest security level, used for critical operations
     */
    MULTI_FACTOR_SIGNATURE("Multi-Factor Signature", "Signature requiring multiple authentication methods"),
    
    /**
     * Time-stamped signature with cryptographic timestamp
     * High security level, used for audit trails
     */
    TIMESTAMPED_SIGNATURE("Time-Stamped Signature", "Signature with cryptographic timestamp"),
    
    /**
     * Qualified electronic signature meeting legal requirements
     * Highest security level, used for legal documents
     */
    QUALIFIED_SIGNATURE("Qualified Signature", "Qualified electronic signature meeting legal requirements");

    private final String displayName;
    private final String description;

    SignatureType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get the security level of this signature type
     */
    public SecurityLevel getSecurityLevel() {
        switch (this) {
            case QUALIFIED_SIGNATURE:
            case MULTI_FACTOR_SIGNATURE:
                return SecurityLevel.HIGHEST;
            case DIGITAL_SIGNATURE:
            case BIOMETRIC_SIGNATURE:
            case TIMESTAMPED_SIGNATURE:
                return SecurityLevel.HIGH;
            case DRAWN_SIGNATURE:
            case VOICE_SIGNATURE:
            case IMAGE_SIGNATURE:
                return SecurityLevel.MEDIUM;
            case TYPED_SIGNATURE:
            case CLICK_SIGNATURE:
                return SecurityLevel.BASIC;
            default:
                return SecurityLevel.BASIC;
        }
    }

    /**
     * Check if this signature type requires additional authentication
     */
    public boolean requiresAdditionalAuth() {
        return this == MULTI_FACTOR_SIGNATURE || 
               this == BIOMETRIC_SIGNATURE || 
               this == QUALIFIED_SIGNATURE;
    }

    /**
     * Check if this signature type is legally binding
     */
    public boolean isLegallyBinding() {
        return this == QUALIFIED_SIGNATURE || 
               this == DIGITAL_SIGNATURE || 
               this == TIMESTAMPED_SIGNATURE;
    }

    /**
     * Get the recommended expiration period in days for this signature type
     */
    public int getRecommendedExpirationDays() {
        switch (this) {
            case QUALIFIED_SIGNATURE:
            case DIGITAL_SIGNATURE:
                return 365; // 1 year
            case MULTI_FACTOR_SIGNATURE:
            case BIOMETRIC_SIGNATURE:
                return 180; // 6 months
            case TIMESTAMPED_SIGNATURE:
                return 90; // 3 months
            case DRAWN_SIGNATURE:
            case VOICE_SIGNATURE:
            case IMAGE_SIGNATURE:
                return 30; // 1 month
            case TYPED_SIGNATURE:
            case CLICK_SIGNATURE:
                return 7; // 1 week
            default:
                return 30;
        }
    }

    /**
     * Security levels for signature types
     */
    public enum SecurityLevel {
        BASIC("Basic security level"),
        MEDIUM("Medium security level"),
        HIGH("High security level"),
        HIGHEST("Highest security level");

        private final String description;

        SecurityLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
} 