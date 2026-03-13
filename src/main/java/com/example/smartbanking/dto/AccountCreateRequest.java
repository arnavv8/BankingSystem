package com.example.smartbanking.dto;

import com.example.smartbanking.entity.AccountType;

public class AccountCreateRequest {

    private Long userId;
    private AccountType accountType;

    public Long getUserId() {
        return userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}