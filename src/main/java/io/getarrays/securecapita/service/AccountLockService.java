package io.getarrays.securecapita.service;

import io.getarrays.securecapita.domain.User;
import io.getarrays.securecapita.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing account lockout functionality
 * Implements brute force protection with automatic unlock
 * 
 * @author Generated
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountLockService {

    private final UserRepository<User> userRepository;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES = 30;

    /**
     * Handle failed login attempt
     * @param email user email
     * @return true if account is now locked, false otherwise
     */
    public boolean handleFailedLogin(String email) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                user.incrementFailedLoginAttempts();
                userRepository.save(user);
                
                if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                    log.warn("Account locked for user: {} after {} failed attempts", 
                            email, user.getFailedLoginAttempts());
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("Error handling failed login for user: {}", email, e);
            return false;
        }
    }

    /**
     * Handle successful login - reset failed attempts
     * @param email user email
     */
    public void handleSuccessfulLogin(String email) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null && user.getFailedLoginAttempts() > 0) {
                user.resetFailedLoginAttempts();
                userRepository.save(user);
                log.info("Reset failed login attempts for user: {}", email);
            }
        } catch (Exception e) {
            log.error("Error handling successful login for user: {}", email, e);
        }
    }

    /**
     * Check if account is locked
     * @param email user email
     * @return true if account is locked, false otherwise
     */
    public boolean isAccountLocked(String email) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                // Check if lockout period has expired
                if (user.shouldAutoUnlock()) {
                    unlockAccount(email);
                    return false;
                }
                return user.isAccountLocked();
            }
            return false;
        } catch (Exception e) {
            log.error("Error checking account lock status for user: {}", email, e);
            return false;
        }
    }

    /**
     * Manually unlock account
     * @param email user email
     * @return true if unlocked successfully, false otherwise
     */
    public boolean unlockAccount(String email) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                user.unlockAccount();
                userRepository.save(user);
                log.info("Account unlocked for user: {}", email);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error unlocking account for user: {}", email, e);
            return false;
        }
    }

    /**
     * Lock account manually
     * @param email user email
     * @param minutes lockout duration in minutes
     * @return true if locked successfully, false otherwise
     */
    public boolean lockAccount(String email, int minutes) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                user.lockAccount(minutes);
                userRepository.save(user);
                log.info("Account manually locked for user: {} for {} minutes", email, minutes);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Error locking account for user: {}", email, e);
            return false;
        }
    }

    /**
     * Get account lockout status
     * @param email user email
     * @return AccountLockoutStatus object with details
     */
    public AccountLockoutStatus getAccountStatus(String email) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                return AccountLockoutStatus.builder()
                        .email(email)
                        .isLocked(user.isAccountLocked())
                        .failedAttempts(user.getFailedLoginAttempts())
                        .remainingMinutes(user.getRemainingLockoutMinutes())
                        .lastFailedLogin(user.getLastFailedLogin())
                        .lockoutUntil(user.getAccountLockedUntil())
                        .build();
            }
            return AccountLockoutStatus.builder()
                    .email(email)
                    .isLocked(false)
                    .failedAttempts(0)
                    .remainingMinutes(0)
                    .build();
        } catch (Exception e) {
            log.error("Error getting account status for user: {}", email, e);
            return AccountLockoutStatus.builder()
                    .email(email)
                    .isLocked(false)
                    .failedAttempts(0)
                    .remainingMinutes(0)
                    .build();
        }
    }

    /**
     * Auto-unlock expired accounts (can be called by scheduled task)
     */
    public void autoUnlockExpiredAccounts() {
        try {
            @SuppressWarnings("unchecked")
            List<User> lockedUsers = userRepository.findLockedUsers();
            int unlockedCount = 0;
            
            for (User user : lockedUsers) {
                if (user.shouldAutoUnlock()) {
                    user.unlockAccount();
                    userRepository.save(user);
                    unlockedCount++;
                    log.info("Auto-unlocked expired account for user: {}", user.getEmail());
                }
            }
            
            if (unlockedCount > 0) {
                log.info("Auto-unlocked {} expired accounts", unlockedCount);
            }
        } catch (Exception e) {
            log.error("Error auto-unlocking expired accounts", e);
        }
    }

    /**
     * Get remaining attempts before lockout
     * @param email user email
     * @return number of remaining attempts
     */
    public int getRemainingAttempts(String email) {
        try {
            User user = userRepository.getUserByEmail(email);
            if (user != null) {
                return Math.max(0, MAX_FAILED_ATTEMPTS - user.getFailedLoginAttempts());
            }
            return MAX_FAILED_ATTEMPTS;
        } catch (Exception e) {
            log.error("Error getting remaining attempts for user: {}", email, e);
            return MAX_FAILED_ATTEMPTS;
        }
    }

    /**
     * DTO for account lockout status
     */
    @lombok.Builder
    @lombok.Data
    public static class AccountLockoutStatus {
        private String email;
        private boolean isLocked;
        private int failedAttempts;
        private long remainingMinutes;
        private LocalDateTime lastFailedLogin;
        private LocalDateTime lockoutUntil;
    }
}
