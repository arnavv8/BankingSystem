package com.example.smartbanking.dto;

public class RegisterRequest {

    private String fullName;   // name
    private String email;      // email
    private String password;   // raw password

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}