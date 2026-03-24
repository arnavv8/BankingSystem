package com.example.smartbanking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique bank account number
    @Column(nullable = false, unique = true)
    private String accountNumber;

    // SAVINGS, CURRENT, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    // PENDING, APPROVED, REJECTED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountStatus status = AccountStatus.PENDING;

    // Account balance
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    // Account creation timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // For admin/fraud freeze
    @Column(nullable = false)
    private Boolean frozen = false;

    // Many accounts → one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // -------------------- Constructors --------------------

    public Account() {
    }

    public Account(String accountNumber, AccountType accountType, User user) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.user = user;
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.PENDING;
        this.frozen = false;
    }

    // -------------------- Lifecycle Hooks --------------------

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        if (this.status == null) {
            this.status = AccountStatus.PENDING;
        }
        if (this.frozen == null) {
            this.frozen = false;
        }
    }

    // -------------------- Getters & Setters --------------------

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}