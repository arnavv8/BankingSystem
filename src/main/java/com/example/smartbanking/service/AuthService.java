package com.example.smartbanking.service;

import com.example.smartbanking.dto.LoginRequest;
import com.example.smartbanking.dto.OTPVerificationRequest;
import com.example.smartbanking.dto.RegisterRequest;
import com.example.smartbanking.dto.TokenResponse;

public interface AuthService {

    /**
     * Registers a new user, saves hashed password, and sends OTP.
     */
    void register(RegisterRequest request);

    /**
     * Logs in using email + password and returns JWT access token.
     */
    TokenResponse login(LoginRequest request);

    /**
     * Sends (or resends) OTP to the user's email for verification/login.
     */
    void sendOtp(String email);

    /**
     * Verifies OTP for registration or login.
     * If valid, returns JWT token.
     */
    TokenResponse verifyOtp(OTPVerificationRequest request);
}