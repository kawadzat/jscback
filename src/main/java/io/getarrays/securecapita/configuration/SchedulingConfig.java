package io.getarrays.securecapita.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class to enable scheduling for repetitive task reminders
 * 
 * @author SecureCapita
 * @version 1.0
 * @since 2024
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // This enables the @Scheduled annotation functionality
} 