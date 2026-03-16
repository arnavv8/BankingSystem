package com.example.smartbanking.service.impl;

import com.example.smartbanking.entity.Account;
import com.example.smartbanking.entity.AccountType;
import com.example.smartbanking.repository.AccountRepository;
import com.example.smartbanking.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Long userId, AccountType accountType) {
        // TODO: actual implementation
        return null;
    }

    @Override
    public List<Account> getAccountsForUser(Long userId) {
        // TODO: actual implementation
        return List.of();
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        // TODO: actual implementation
        return null;
    }

    @Override
    public void freezeAccount(String accountNumber) {
        // TODO: actual implementation
    }

    @Override
    public void unfreezeAccount(String accountNumber) {
        // TODO: actual implementation
    }
}