package com.example.smartbanking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.function.Supplier;

@Configuration
public class AppConfig {

    /**
     * System clock (UTC) – handy for time-based logic (OTP expiry, audit timestamps),
     * and makes time testable by injecting a different Clock in unit tests.
     */
    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    /**
     * Cryptographically strong RNG for security-sensitive operations
     * (OTP generation, reference IDs, etc.).
     */
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    /**
     * OTP generator bean (6-digit, zero-padded).
     * Usage: otpCodeSupplier.get() -> e.g., "034217"
     */
    @Bean
    public Supplier<String> otpCodeSupplier(SecureRandom secureRandom) {
        return () -> {
            int n = secureRandom.nextInt(1_000_000); // 0..999999
            return String.format("%06d", n);
        };
    }

    /**
     * Pre-configured ObjectMapper with Java Time support,
     * so LocalDate/LocalDateTime serialize/deserialize cleanly.
     */
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    /**
     * RestTemplate with sane defaults for any outbound HTTP calls
     * (e.g., email providers, chatbot backends if you add them later).
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}