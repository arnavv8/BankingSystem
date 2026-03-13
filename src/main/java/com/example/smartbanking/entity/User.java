package com.example.smartbanking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Full name of the user
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String fullName;

    // Unique email used for login & OTP
    @Email
    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150, unique = true)
    private String email;

    // BCrypt-hashed password (store hash, never plain text)
    @NotBlank
    @Size(min = 8, max = 255)
    @Column(nullable = false, length = 255)
    private String passwordHash;

    // Optional phone for notifications / OTP fallback
    @Size(max = 20)
    @Column(length = 20)
    private String phone;

    // Whether the user completed email OTP verification
    @Column(nullable = false)
    private boolean verified = false;

    // Timestamps
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Roles for authorization (e.g., USER, ADMIN)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<Role> roles = new HashSet<>();

    // One user can have many accounts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    // One user can have many OTPs (login, verification, reset)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OTP> otps = new ArrayList<>();

    // -------------------- Constructors --------------------

    public User() {
    }

    public User(String fullName, String email, String passwordHash) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles.add(Role.USER);
    }

    // -------------------- Utility Methods --------------------

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addAccount(Account account) {
        if (account == null) return;
        accounts.add(account);
        account.setUser(this);
    }

    public void removeAccount(Account account) {
        if (account == null) return;
        accounts.remove(account);
        account.setUser(null);
    }

    public void addOtp(OTP otp) {
        if (otp == null) return;
        otps.add(otp);
        otp.setUser(this);
    }

    public void removeOtp(OTP otp) {
        if (otp == null) return;
        otps.remove(otp);
        otp.setUser(null);
    }

    // -------------------- Getters & Setters --------------------

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /** Set BCrypt-hashed password here (never plain text). */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        if (roles == null) {
            this.roles = new HashSet<>();
        } else {
            this.roles = roles;
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<OTP> getOtps() {
        return otps;
    }
}