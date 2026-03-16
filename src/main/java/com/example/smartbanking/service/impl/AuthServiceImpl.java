package com.example.smartbanking.service.impl;

import com.example.smartbanking.dto.LoginRequest;
import com.example.smartbanking.dto.OTPVerificationRequest;
import com.example.smartbanking.dto.RegisterRequest;
import com.example.smartbanking.dto.TokenResponse;
import com.example.smartbanking.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public void register(RegisterRequest request) {
        // TODO: later
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        return null; // TODO later
    }

    @Override
    public void sendOtp(String email) {
        // TODO later
    }

    @Override
    public TokenResponse verifyOtp(OTPVerificationRequest request) {
        return null; // TODO later
    }
}