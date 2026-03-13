package com.example.smartbanking.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Debit or Credit type
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // Reference number for audit/fraud tracking
    @Column(nullable = false, unique = true)
    private String referenceId;

    // Balance after this transaction
    @Column(nullable = false)
    private BigDecimal postBalance;

    // Optional remark (e.g., "Transfer to 987654321")
    private String description;

    // Many transactions to one account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction() {
    }

    public Transaction(TransactionType type,
                       BigDecimal amount,
                       BigDecimal postBalance,
                       String referenceId,
                       String description,
                       Account account) {

        this.type = type;
        this.amount = amount;
        this.postBalance = postBalance;
        this.referenceId = referenceId;
        this.description = description;
        this.account = account;
        this.timestamp = LocalDateTime.now();
    }

    // ---------------- GETTERS & SETTERS ----------------

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public BigDecimal getPostBalance() {
        return postBalance;
    }

    public void setPostBalance(BigDecimal postBalance) {
        this.postBalance = postBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}