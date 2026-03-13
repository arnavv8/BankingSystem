package com.example.smartbanking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The actual OTP code
    @Column(nullable = false)
    private String code;

    // When the OTP expires
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // If the OTP was used successfully
    @Column(nullable = false)
    private boolean used = false;

    // Many OTPs can belong to one user (for login, password reset, etc.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public OTP() {
    }

    public OTP(String code, LocalDateTime expiresAt, User user) {
        this.code = code;
        this.expiresAt = expiresAt;
        this.user = user;
        this.used = false;
    }

    // ------------------ GETTERS & SETTERS ------------------

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}