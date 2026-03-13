package com.example.smartbanking.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OTPGenerator {

    private final SecureRandom random;

    @Value("${app.otp.length:6}")
    private int defaultLength; // default digits

    public OTPGenerator(SecureRandom random) {
        this.random = random;
    }

    public String numeric() {
        return numeric(defaultLength);
    }

    public String numeric(int length) {
        if (length <= 0) throw new IllegalArgumentException("length > 0");
        char[] digits = new char[length];
        for (int i = 0; i < length; i++) {
            digits[i] = (char) ('0' + random.nextInt(10));
        }
        return new String(digits);
    }

    public String alphanumeric(int length) {
        if (length <= 0) throw new IllegalArgumentException("length > 0");
        final char[] alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] out = new char[length];
        for (int i = 0; i < length; i++) {
            out[i] = alphabet[random.nextInt(alphabet.length)];
        }
        return new String(out);
    }
}