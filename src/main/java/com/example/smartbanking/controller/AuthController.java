package com.example.smartbanking.controller;

import com.example.smartbanking.dto.ApiResponse;
import com.example.smartbanking.dto.EmailRequest;
import com.example.smartbanking.dto.LoginRequest;
import com.example.smartbanking.dto.OTPVerificationRequest;
import com.example.smartbanking.dto.RegisterRequest;
import com.example.smartbanking.dto.TokenResponse;
import com.example.smartbanking.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication & onboarding endpoints:
 * - Register (sends OTP)
 * - Login (returns JWT)
 * - Send/Resend OTP
 * - Verify OTP (returns JWT after verification)
 * - Logout (client-side token discard; server is stateless)
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Registration, login, OTP verification")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user and send OTP to the registered email.
     */
    @Operation(summary = "Register new user and send OTP")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(new ApiResponse("Registration successful. OTP sent to your email."));
    }

    /**
     * Login with email & password. Returns JWT access token on success.
     */
    @Operation(summary = "Login with email & password")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    /**
     * Send or resend OTP to the user's email (for email verification or login OTP flow).
     */
    @Operation(summary = "Send/Resend OTP to email")
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@Valid @RequestBody EmailRequest request) {
        authService.sendOtp(request.getEmail());
        return ResponseEntity.ok(new ApiResponse("OTP sent to " + request.getEmail()));
    }

    /**
     * Verify an OTP. If verification succeeds, returns a JWT (so the user is immediately logged in).
     */
    @Operation(summary = "Verify OTP (email verification or login)")
    @PostMapping("/verify-otp")
    public ResponseEntity<TokenResponse> verifyOtp(@Valid @RequestBody OTPVerificationRequest request) {
        TokenResponse token = authService.verifyOtp(request);
        return ResponseEntity.ok(token);
    }

    /**
     * Logout endpoint (stateless): client should drop the token.
     */
    @Operation(summary = "Logout (stateless; discard token on client)")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout() {
        return ResponseEntity.ok(new ApiResponse("Logged out"));
    }
}