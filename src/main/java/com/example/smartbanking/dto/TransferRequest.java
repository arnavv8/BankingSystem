package com.example.smartbanking.dto;

import java.math.BigDecimal;

public class TransferRequest {

    private String fromAccount; // source
    private String toAccount;   // destination
    private BigDecimal amount;  // amount
    private String description; // note

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}