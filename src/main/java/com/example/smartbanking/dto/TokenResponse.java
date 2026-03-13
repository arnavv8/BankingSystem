package com.example.smartbanking.dto;

public class TokenResponse {

    private String accessToken; // jwt
    private long expiresIn;     // expiry seconds

    public TokenResponse(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}