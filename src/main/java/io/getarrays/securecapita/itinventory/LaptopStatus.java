package io.getarrays.securecapita.itinventory;

public enum LaptopStatus {
    AVAILABLE,
    ISSUE,                   // When the laptop is being issued (initial request)
    PENDING_ACKNOWLEDGMENT,  // Waiting for acknowledgment after issue
    ISSUED,                  // Fully issued and acknowledged
    MAINTENANCE,
    RETIRED,
    ACTIVE,                  // Laptop is active
    INACTIVE                 // Laptop is inactive
    
}
