package com.example.smartbanking.service.impl;

import com.example.smartbanking.dto.LoginRequest;
import com.example.smartbanking.dto.OTPVerificationRequest;
import com.example.smartbanking.dto.RegisterRequest;
import com.example.smartbanking.dto.TokenResponse;
import com.example.smartbanking.security.CustomerUserDetailsService;
import com.example.smartbanking.security.JwtService;
import com.example.smartbanking.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomerUserDetailsService userDetailsService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           CustomerUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void register(RegisterRequest request) {

    }

    @Override
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        return new TokenResponse(token);
    }

    @Override
    public void sendOtp(String email) {

    }

    @Override
    public TokenResponse verifyOtp(OTPVerificationRequest request) {
        return null;
    }
}